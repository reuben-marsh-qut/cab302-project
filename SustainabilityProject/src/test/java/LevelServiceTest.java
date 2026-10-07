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
        assertEquals(1, levelService.getLevel(0));
    }

    @Test
    void xpBelowFirstThresholdRemainsAtLevelOne() {
        assertEquals(1, levelService.getLevel(99));
    }

    @Test
    void reachingFirstThresholdIncreasesLevelToTwo() {
        assertEquals(2, levelService.getLevel(100));
    }

    @Test
    void xpBelowSecondThresholdRemainsAtLevelTwo() {
        assertEquals(2, levelService.getLevel(299));
    }

    @Test
    void reachingSecondThresholdIncreasesLevelToThree() {
        assertEquals(3, levelService.getLevel(300));
    }

    @Test
    void xpBelowThirdThresholdRemainsAtLevelThree() {
        assertEquals(3, levelService.getLevel(599));
    }

    @Test
    void reachingThirdThresholdIncreasesLevelToFour() {
        assertEquals(4, levelService.getLevel(600));
    }

    @Test
    void totalXpCanDetermineLevelAcrossMultipleThresholds() {
        assertEquals(5, levelService.getLevel(1000));
    }
}