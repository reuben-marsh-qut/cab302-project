import com.example.cab302project.model.GoalTemplate;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GoalTemplateTest {

    @Test
    public void testGetTemplatesForCategoryReturnsOnlyThatCategory() {
        List<GoalTemplate> bodyTemplates =
                GoalTemplate.getTemplatesForCategory(Category.BODY);

        for (GoalTemplate template : bodyTemplates) {
            assertEquals(Category.BODY, template.getCategory());
        }
    }

    @Test
    public void testEachCategoryHasThreeTemplates() {
        assertEquals(3, GoalTemplate.getTemplatesForCategory(Category.MIND).size());
        assertEquals(3, GoalTemplate.getTemplatesForCategory(Category.BODY).size());
        assertEquals(3, GoalTemplate.getTemplatesForCategory(Category.WORLD).size());
    }

    @Test
    public void testCategoryWithNoTemplatesReturnsEmptyList() {
        List<GoalTemplate> socialTemplates =
                GoalTemplate.getTemplatesForCategory(Category.SOCIAL);

        assertNotNull(socialTemplates);
        assertTrue(socialTemplates.isEmpty());
    }

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
    public void testOneAndDoneTemplatesHaveTargetOfOne() {
        List<GoalTemplate> mindTemplates =
                GoalTemplate.getTemplatesForCategory(Category.MIND);

        for (GoalTemplate template : mindTemplates) {
            if (template.getCompletionType() == CompletionType.BINARY) {
                assertEquals(1, template.getTarget());
            }
        }
    }
}