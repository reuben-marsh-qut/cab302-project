import com.example.cab302project.model.User;
import com.example.cab302project.model.UserSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests storage and clearing of the currently logged-in user.
 */
public class UserSessionTest {

    private UserSession userSession;

    /**
     * Clears the shared session before each test.
     */
    @BeforeEach
    void setUp() {
        userSession = UserSession.getInstance();
        userSession.clearUserSession();
    }

    /**
     * Prevents session state from leaking into other tests.
     */
    @AfterEach
    void tearDown() {
        userSession.clearUserSession();
    }

    /**
     * Verifies that the session stores the supplied user.
     */
    @Test
    void loggedInUserShouldBeStoredInSession() {
        User user = new User(
                1,
                "test@example.com",
                "unused-test-hash",
                0,
                4000
        );

        userSession.setUser(user);

        assertNotNull(userSession.getUser());
        assertSame(user, userSession.getUser());
    }

    /**
     * Verifies that signing out removes the current user.
     */
    @Test
    void logoutShouldClearCurrentUser() {
        User user = new User(
                1,
                "test@example.com",
                "unused-test-hash",
                0,
                4000
        );

        userSession.setUser(user);
        userSession.clearUserSession();

        assertNull(userSession.getUser());
    }
}