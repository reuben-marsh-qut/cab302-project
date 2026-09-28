package com.example.cab302project.model;

import com.example.cab302project.ModelConnection;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.response.ChatResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * A suggested starting point for a new habit. Templates prefill the goal
 * creation form; the user can change anything before saving.
 */
public class HabitTemplate {
    private String title;
    private Category category;
    private TaskType taskType;
    private int repeatFrequency;
    private RepeatFrequencyType repeatFrequencyType;
    private int target;

    /**
     * Creates a habit template.
     * @param title What the habit suggests doing.
     * @param category The wellbeing area this template belongs to.
     * @param taskType Whether it is worked towards or one and done.
     * @param target The value that must be reached to complete it.
     * @param repeatFrequencyType does it repeat every day, week, month or year.
     * @param repeatFrequency how often the associated task repeats.
     * @throws IllegalArgumentException if any of the details are invalid.
     */
    public HabitTemplate(String title, Category category,
                         TaskType taskType, int target, RepeatFrequencyType repeatFrequencyType, int repeatFrequency) {
        validateTitle(title);
        validateTarget(target);

        this.title = title;
        this.category = category;
        this.taskType = taskType;
        this.target = target;
        this.repeatFrequencyType = repeatFrequencyType;
        this.repeatFrequency = repeatFrequency;
    }

    private static void validateTitle(String templateTitle) {
        if (templateTitle == null || templateTitle.isBlank()) {
            throw new IllegalArgumentException(
                    "Goal template title must not be blank.");
        }
    }

    private static void validateTarget(int templateTarget) {
        if (templateTarget < 1) {
            throw new IllegalArgumentException(
                    "Goal template target must be at least one.");
        }
    }

    /**
     * Returns the suggested templates for one category and completion type.
     * @param category The category to find templates for.
     * @param taskType Whether the goal is worked towards or one and done.
     * @return The matching templates, or an empty list if there are none.
     */
    public static List<HabitTemplate> getTemplatesFor(Category category,
                                                      TaskType taskType) {
        List<HabitTemplate> matching = new ArrayList<>();

        for (HabitTemplate template : allTemplates()) {
            if (template.getCategory() == category
                    && template.getTaskType() == taskType) {
                matching.add(template);
            }
        }

        return matching;
    }

    /**
     * The full set of starter templates the app offers.
     */
    private static List<HabitTemplate> allTemplates() {
        List<HabitTemplate> templates = new ArrayList<>();

        // Mind - worked towards over time
        templates.add(new HabitTemplate("Meditate for 15 minutes each day",
                Category.MIND, TaskType.PROGRESSIVE, 15, RepeatFrequencyType.DAILY, 1));
        templates.add(new HabitTemplate("Read 2 books each week",
                Category.MIND, TaskType.PROGRESSIVE, 2, RepeatFrequencyType.WEEKLY, 1));
        templates.add(new HabitTemplate("Journal twice each day",
                Category.MIND, TaskType.PROGRESSIVE, 2, RepeatFrequencyType.DAILY,1));

        // Mind - one and done
        templates.add(new HabitTemplate("Complete a random act of kindness each month",
                Category.MIND, TaskType.BINARY, 1, RepeatFrequencyType.MONTHLY,1));
        templates.add(new HabitTemplate("Spend spend a day offline each week",
                Category.MIND, TaskType.BINARY, 1, RepeatFrequencyType.WEEKLY, 1));
        templates.add(new HabitTemplate("Practice music every three days",
                Category.MIND, TaskType.BINARY, 1, RepeatFrequencyType.DAILY, 2));

        // Body - worked towards over time
        templates.add(new HabitTemplate("Walk 10, 000 steps each day",
                Category.BODY, TaskType.PROGRESSIVE, 10000, RepeatFrequencyType.DAILY,1));
        templates.add(new HabitTemplate("Cook 5 meals yourself each week",
                Category.BODY, TaskType.PROGRESSIVE, 5, RepeatFrequencyType.WEEKLY,1));
        templates.add(new HabitTemplate("Swim 7 kilometres each week",
                Category.BODY, TaskType.PROGRESSIVE, 6,RepeatFrequencyType.WEEKLY,1));

        // Body - one and done
        templates.add(new HabitTemplate("Go to the gym every two days",
                Category.BODY, TaskType.BINARY, 1, RepeatFrequencyType.DAILY, 2));
        templates.add(new HabitTemplate("Go for a walk outside each day",
                Category.BODY, TaskType.BINARY, 1,RepeatFrequencyType.DAILY,1));
        templates.add(new HabitTemplate("Try a new sport every three months",
                Category.BODY, TaskType.BINARY, 1, RepeatFrequencyType.MONTHLY,3));

        // World - worked towards over time
        templates.add(new HabitTemplate("Swap 5 car trips each week for walking or public transport",
                Category.WORLD, TaskType.PROGRESSIVE, 5, RepeatFrequencyType.WEEKLY, 1));
        templates.add(new HabitTemplate("Avoid 100 single-use plastic items each year",
                Category.WORLD, TaskType.PROGRESSIVE, 100, RepeatFrequencyType.YEARLY,1));
        templates.add(new HabitTemplate("Compost at least 4 kilograms of food waste each month",
                Category.WORLD, TaskType.PROGRESSIVE, 4, RepeatFrequencyType.MONTHLY,1));

        // World - one and done
        templates.add(new HabitTemplate("Compost all your food waste each month",
                Category.WORLD, TaskType.BINARY, 1, RepeatFrequencyType.MONTHLY, 1));
        templates.add(new HabitTemplate("Join a local clean-up day twice a year",
                Category.WORLD, TaskType.BINARY, 1,RepeatFrequencyType.MONTHLY, 6));
        templates.add(new HabitTemplate("Tend to your garden each week",
                Category.WORLD, TaskType.BINARY, 1,RepeatFrequencyType.WEEKLY,1));

        return templates;
    }

    public static List<HabitTemplate> generateHabitsFromGoal(Goal goal, int number){

        return new ArrayList<HabitTemplate>();
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getTarget() {
        return target;
    }

    public int getRepeatFrequency() {
        return repeatFrequency;
    }

    public RepeatFrequencyType getRepeatFrequencyType() {
        return repeatFrequencyType;
    }

    @Override
    public String toString() {
        return "Title: " + title +
                " Catagory: " + category.getLabel() +
                " Task Type: " + taskType.toString() +
                " Target: " + target +
                " repeatFrequencyType: " + repeatFrequencyType.toString() +
                " repeatFrequency: " + repeatFrequency;
    }
    public static HabitTemplate generateHabitFromPartialGoal(String goalTitle, Category goalCatagory){
        return generateHabitFromPartialGoal(goalTitle,goalCatagory,null);
    }
    public static HabitTemplate generateHabitFromPartialGoal(String goalTitle, Category goalCatagory, String doNotGenerateThis){
        ChatModel model = ModelConnection.getInstance().getJSONModel();
        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .name("Habit")
                        .rootElement(JsonObjectSchema.builder()
                                .addStringProperty("title","Describe a beneficial task that can be repeated. e.g. Save $2,000, Walk 10,000 steps")
                                .addEnumProperty("taskType", List.of("BINARY", "PROGRESSIVE"), "Do you progressively work towards completing the task or is completion binary.")
                                .addIntegerProperty("target", "If the task is Progressive what should be the target number for task completion. If the task is Binary this is 1.")
                                .addEnumProperty("repeatFrequencyType", List.of("DAILY","WEEKLY","MONTLY","YEARLY"), "How often should the habit repeat.")
                                .addIntegerProperty("repeatFrequency", "How long should the interval between repeats be. e.g. if repeatFrequencyType is Daily and repeatFrequency is 1 the habit will repeat each day, if repeatFrequency is 2 the habit will repeat every two days.")
                                .build())
                        .build())
                .build();
        SystemMessage systemMessage = SystemMessage.from("""
                Generate a habit from a given goal. The habit should be a beneficial task that can be repeated. e.g. Save $2,000, Walk 10,000 steps.
                Do not generate NULL or null;
                
                BAD:
                {
                  "title": "Daily Movement Minimum", // WRONG: the title does not describe what the user must do
                  "taskType": "PROGRESSIVE",
                  "target": 15,
                  "repeatFrequencyType": "DAILY",
                  "repeatFrequency": 7 // WRONG: this means this will repeat every seven days instead of every 1 day
                }
                {
                  "title": "Write down three things you are grateful for each day",
                  "taskType": "BINARY", 
                  "target": 3, // WRONG: the target should be 1 if the task is binary
                  "repeatFrequencyType": "DAILY",
                  "repeatFrequency": 1
                }
                GOOD:
                Catagory: BODY
                {
                  "title": "Move for at least 15 minutes each day",
                  "taskType": "PROGRESSIVE",
                  "target": 15,
                  "repeatFrequencyType": "DAILY",
                  "repeatFrequency": 1
                }
                Catagory: MIND
                {
                  "title": "Complete a random act of kindness each month",
                  "taskType": "BINARY",
                  "target": 1,
                  "repeatFrequencyType": "MONTHLY",
                  "repeatFrequency": 1
                }
                Catagory: WORLD
                {
                  "title": "Swap at least 5 car trips each week for walking or public transport",
                  "taskType": "PROGRESSIVE",
                  "target": 5,
                  "repeatFrequencyType": "WEEKLY",
                  "repeatFrequency": 1
                }
               Catagory: MIND
                {
                  "title": "Journal for 10 minutes each day",
                  "taskType": "BINARY",
                  "target": 1,
                  "repeatFrequencyType": "DAILY",
                  "repeatFrequency": 1
                }
                """);
        ChatRequest chatRequest;
        UserMessage userMessage = UserMessage.from(String.format("Generate a Habit from the goal '%s', the catagory of the goal is '%s'",goalTitle,goalCatagory));

        if (doNotGenerateThis != null){
            SystemMessage preventDuplicates = new SystemMessage(String.format("""
                    Do NOT duplicate the following habit/s as they have already been presented to the user. Please think of a distinct habit.
                    %s
                    """,doNotGenerateThis));
            chatRequest = ChatRequest.builder()
                    .responseFormat(responseFormat)
                    .messages(systemMessage,preventDuplicates,userMessage)
                    .build();
        } else {
            chatRequest = ChatRequest.builder()
                    .responseFormat(responseFormat)
                    .messages(systemMessage,userMessage)
                    .build();
        }

        ChatResponse response = model.chat(chatRequest);
        String output = response.aiMessage().text();
//        System.out.println(output);
        try {
            PartialHabitTemplate partialHabitTemplate = new ObjectMapper().readValue(output, PartialHabitTemplate.class);
            HabitTemplate template = new HabitTemplate(partialHabitTemplate.title(), goalCatagory,partialHabitTemplate.taskType(), partialHabitTemplate.taskType() == TaskType.PROGRESSIVE ? partialHabitTemplate.target() : 1, partialHabitTemplate.repeatFrequencyType(),partialHabitTemplate.repeatFrequency());
//            System.out.println(template);
            return template;
        }   catch (Exception e){
            System.err.println(e.toString());
            return null;
//            throw e;
        }
    }

    public static record PartialHabitTemplate(String title, TaskType taskType, int target, RepeatFrequencyType repeatFrequencyType, int repeatFrequency){}
}