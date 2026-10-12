import com.example.cab302project.model.GoalTemplate;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GoalTemplateTest {


    @Test
    public void testBlankTitleThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new GoalTemplate("  ", Category.MIND,
                        CompletionType.PROGRESSIVE, 600));
    }

    @Test
    public void testTargetBelowOneThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new GoalTemplate("Meditate for 600 minutes", Category.MIND,
                        CompletionType.PROGRESSIVE, 0));
    }

    @Test
    public void testGetTemplatesForReturnsOnlyMatchingCategoryAndType() {
        List<GoalTemplate> templates =
                GoalTemplate.getTemplatesFor(Category.BODY, CompletionType.BINARY);

        for (GoalTemplate template : templates) {
            assertEquals(Category.BODY, template.getCategory());
            assertEquals(CompletionType.BINARY, template.getCompletionType());
        }
    }

    @Test
    public void testEachCategoryAndTypeHasThreeTemplates() {
        Category[] categories = { Category.MIND, Category.BODY, Category.WORLD };

        for (Category category : categories) {
            assertEquals(3,
                    GoalTemplate.getTemplatesFor(category, CompletionType.PROGRESSIVE).size());
            assertEquals(3,
                    GoalTemplate.getTemplatesFor(category, CompletionType.BINARY).size());
        }
    }

    @Test
    public void testCategoryWithNoTemplatesReturnsEmptyList() {
        List<GoalTemplate> socialTemplates =
                GoalTemplate.getTemplatesFor(Category.SOCIAL, CompletionType.PROGRESSIVE);

        assertNotNull(socialTemplates);
        assertTrue(socialTemplates.isEmpty());
    }

    @Test
    public void testOneAndDoneTemplatesHaveTargetOfOne() {
        Category[] categories = { Category.MIND, Category.BODY, Category.WORLD };

        for (Category category : categories) {
            for (GoalTemplate template :
                    GoalTemplate.getTemplatesFor(category, CompletionType.BINARY)) {
                assertEquals(1, template.getTarget());
            }
        }
    }
}