import com.example.cab302project.model.LevelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LevelServiceTest {

    private LevelService levelService;

    @BeforeEach
    void setUp() {
        levelService = new LevelService();
    }

    @Test
    void zeroXpStartsAtLevelOne() {
        int level = levelService.getLevel(0);

        assertEquals(1, level);
    }

    @Test
    void xpBelowFirstThresholdRemainsAtLevelOne() {
        int level = levelService.getLevel(99);

        assertEquals(1, level);
    }

    @Test
    void reachingFirstThresholdIncreasesLevelToTwo() {
        int level = levelService.getLevel(100);

        assertEquals(2, level);
    }
}