package com.example.cab302project.controller;

import com.example.cab302project.model.Activity;
import com.example.cab302project.model.ActivityDAO;
import com.example.cab302project.model.IActivityDAO;
import com.example.cab302project.model.ProgressService;
import com.example.cab302project.model.User;
import com.example.cab302project.model.UserSession;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * Displays the authenticated user's daily activity completion report.
 *
 * <p>Completed activities are loaded from the database. Daily counts are
 * calculated by {@link ProgressService}, keeping reporting calculations
 * separate from JavaFX controls.</p>
 */
public class ProgressController {

    private static final int MAX_REPORT_DAYS = 366;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final DateTimeFormatter SHORT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d MMM");

    private static final DateTimeFormatter SHORT_DATE_WITH_YEAR_FORMAT =
            DateTimeFormatter.ofPattern("d MMM yy");

    private static final System.Logger LOGGER =
            System.getLogger(ProgressController.class.getName());

    private final IActivityDAO activityDAO = new ActivityDAO();
    private final ProgressService progressService = new ProgressService();

    private int displayedDayCount;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private ScrollPane chartScrollPane;

    @FXML
    private BarChart<String, Number> completionChart;

    @FXML
    private CategoryAxis completionDateAxis;

    @FXML
    private NumberAxis completionAxis;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label undatedLabel;

    /**
     * Configures the chart and loads the last 30 calendar days,
     * including today.
     */
    @FXML
    public void initialize() {
        LocalDate today = LocalDate.now();

        fromDatePicker.setEditable(false);
        toDatePicker.setEditable(false);

        fromDatePicker.setValue(today.minusDays(29));
        toDatePicker.setValue(today);

        completionChart.setAnimated(false);

        completionDateAxis.setAnimated(false);
        completionDateAxis.setTickLabelsVisible(true);
        completionDateAxis.setTickMarkVisible(true);
        completionDateAxis.setTickLabelRotation(-55);

        completionAxis.setAnimated(false);
        completionAxis.setAutoRanging(false);
        completionAxis.setLowerBound(0);
        completionAxis.setUpperBound(1);
        completionAxis.setTickUnit(1);
        completionAxis.setTickLabelsVisible(true);
        completionAxis.setTickMarkVisible(true);
        completionAxis.setMinorTickVisible(false);

        chartScrollPane.viewportBoundsProperty().addListener(
                (observable, previousBounds, updatedBounds) ->
                        resizeChart()
        );

        onRefreshButtonClick();
    }

    /**
     * Validates the selected dates and refreshes the authenticated user's
     * progress report.
     *
     * <p>The chart supports at most 366 days per report. Historical
     * completions with unknown dates are excluded from the chart and
     * identified in a separate message.</p>
     */
    @FXML
    protected void onRefreshButtonClick() {
        clearReport();

        User currentUser = UserSession.getInstance().getUser();

        if (currentUser == null) {
            showStatus("Sign in to view your progress.");
            return;
        }

        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (fromDate == null || toDate == null) {
            showStatus("Choose both a start date and an end date.");
            return;
        }

        if (toDate.isBefore(fromDate)) {
            showStatus("End date must be on or after start date.");
            return;
        }

        long numberOfDays = ChronoUnit.DAYS.between(fromDate, toDate) + 1;

        if (numberOfDays > MAX_REPORT_DAYS) {
            showStatus("Choose a date range of 366 days or fewer.");
            return;
        }

        try {
            List<Activity> completedActivities =
                    activityDAO.getCompletedActivitiesForUser(
                            currentUser.getUserId()
                    );

            Map<LocalDate, Integer> dailyCounts =
                    progressService.getDailyCompletions(
                            completedActivities,
                            currentUser.getUserId(),
                            fromDate,
                            toDate
                    );

            displayReport(dailyCounts, fromDate, toDate);
            displayUndatedNotice(completedActivities);
        } catch (RuntimeException exception) {
            clearReport();
            showStatus("Could not load your progress. Please try again.");

            LOGGER.log(
                    System.Logger.Level.ERROR,
                    "Could not load the activity progress report.",
                    exception
            );
        }
    }

    /**
     * Displays daily counts and their total for the selected period.
     *
     * @param dailyCounts daily completion counts in chronological order
     * @param fromDate the first included date
     * @param toDate the last included date
     */
    private void displayReport(
            Map<LocalDate, Integer> dailyCounts,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Completed activities");

        DateTimeFormatter axisDateFormat =
                fromDate.getYear() == toDate.getYear()
                        ? SHORT_DATE_FORMAT
                        : SHORT_DATE_WITH_YEAR_FORMAT;

        long total = 0;
        int largestDailyCount = 0;

        for (Map.Entry<LocalDate, Integer> entry : dailyCounts.entrySet()) {
            LocalDate date = entry.getKey();
            int count = entry.getValue();

            XYChart.Data<String, Number> data = new XYChart.Data<>(
                    date.format(axisDateFormat),
                    count
            );

            // JavaFX creates the bar node when the data joins the chart.
            data.nodeProperty().addListener(
                    (observable, previousNode, updatedNode) -> {
                        if (updatedNode != null) {
                            configureBar(updatedNode, date, count);
                        }
                    }
            );

            series.getData().add(data);

            total += count;
            largestDailyCount = Math.max(largestDailyCount, count);
        }

        double tickUnit = Math.max(
                1,
                Math.ceil(largestDailyCount / 5.0)
        );

        double upperBound = Math.max(
                tickUnit,
                Math.ceil((largestDailyCount + 1.0) / tickUnit) * tickUnit
        );

        completionAxis.setTickUnit(tickUnit);
        completionAxis.setUpperBound(upperBound);

        displayedDayCount = dailyCounts.size();

        // Avoid a very wide bar when displaying only a few dates.
        completionChart.setCategoryGap(
                displayedDayCount <= 7 ? 28 : 5
        );

        completionChart.getData().add(series);

        resizeChart();
        chartScrollPane.setHvalue(0);

        summaryLabel.setText(
                total + (total == 1
                        ? " activity completed"
                        : " activities completed")
                        + " · "
                        + fromDate.format(DATE_FORMAT)
                        + " – "
                        + toDate.format(DATE_FORMAT)
        );

        if (total == 0) {
            showStatus(
                    "No dated activity completions in this period. "
                            + "Try another date range or complete an activity."
            );
        }
    }

    /**
     * Adds the exact completion date and count to a bar's tooltip
     * and accessible description.
     *
     * @param bar the JavaFX bar node
     * @param date the completion date
     * @param count the number of completed activities
     */
    private void configureBar(Node bar, LocalDate date, int count) {
        String description = date.format(DATE_FORMAT)
                + ": "
                + count
                + (count == 1
                ? " activity completed"
                : " activities completed");

        Tooltip.install(bar, new Tooltip(description));
        bar.setAccessibleText(description);
    }

    /**
     * Fits reports of up to 31 days into the available panel width.
     *
     * <p>Longer reports retain enough width for readable daily bars
     * and can be scrolled horizontally.</p>
     */
    private void resizeChart() {
        double viewportWidth =
                chartScrollPane.getViewportBounds().getWidth();

        double requiredWidth = displayedDayCount > 31
                ? displayedDayCount * 26.0 + 100.0
                : 0.0;

        completionChart.setPrefWidth(
                Math.max(
                        320.0,
                        Math.max(viewportWidth - 2.0, requiredWidth)
                )
        );
    }

    /**
     * Explains why completed activities without timestamps are not charted.
     *
     * <p>The count covers all supplied completed activities, since undated
     * records cannot be assigned to the selected period.</p>
     *
     * @param completedActivities the user's completed activities
     */
    private void displayUndatedNotice(List<Activity> completedActivities) {
        long undatedCount = completedActivities.stream()
                .filter(activity -> activity.getCompletedAt() == null)
                .count();

        if (undatedCount == 0) {
            return;
        }

        undatedLabel.setText(
                undatedCount
                        + (undatedCount == 1
                        ? " completed activity has"
                        : " completed activities have")
                        + " no recorded completion date and "
                        + (undatedCount == 1 ? "is" : "are")
                        + " excluded from the chart."
        );

        undatedLabel.setVisible(true);
        undatedLabel.setManaged(true);
    }

    /**
     * Removes previous report data and messages before another refresh.
     */
    private void clearReport() {
        completionChart.getData().clear();
        completionAxis.setUpperBound(1);
        completionAxis.setTickUnit(1);

        displayedDayCount = 0;
        resizeChart();

        summaryLabel.setText("");

        statusLabel.setText("");
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        undatedLabel.setText("");
        undatedLabel.setVisible(false);
        undatedLabel.setManaged(false);
    }

    /**
     * Displays a validation, empty-state or loading-error message.
     *
     * @param message the message to display
     */
    private void showStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }
}