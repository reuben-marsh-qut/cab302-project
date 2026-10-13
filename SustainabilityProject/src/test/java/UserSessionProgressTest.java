import com.example.cab302project.model.User;
import com.example.cab302project.model.UserSession;
import javafx.beans.value.ChangeListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests notifications when the session receives refreshed user data.
 */
public class UserSessionProgressTest {

    private UserSession session;

    /**
     * Resets the shared session before each test.
     */
    @BeforeEach
    void setUp() {
        session = UserSession.getInstance();
        session.clearUserSession();
    }

    /**
     * Clears the shared session after each test.
     */
    @AfterEach
    void tearDown() {
        session.clearUserSession();
    }

    /**
     * Verifies that observers receive the refreshed user after
     * the session's user is replaced.
     */
    @Test
    void replacingUserNotifiesObserversWithUpdatedXp() {
        User originalUser = new User(
                1,
                "test@example.com",
                "unused-test-hash",
                100,
                4000
        );

        session.setUser(originalUser);

        AtomicReference<User> observedUser = new AtomicReference<>();

        ChangeListener<User> listener =
                (observable, previousUser, updatedUser) ->
                        observedUser.set(updatedUser);

        session.userProperty().addListener(listener);

        try {
            User refreshedUser = new User(
                    1,
                    "test@example.com",
                    "unused-test-hash",
                    120,
                    4000
            );

            session.setUser(refreshedUser);

            assertSame(
                    refreshedUser,
                    observedUser.get(),
                    "Observers should receive the refreshed user"
            );
        } finally {
            session.userProperty().removeListener(listener);
        }
    }
}