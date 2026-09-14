import com.example.cab302project.model.User;
import com.example.cab302project.model.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserSessionTest {

    private UserSession userSession;

    @BeforeEach
    void setUp() {
        userSession = UserSession.getInstance();
        userSession.clearUserSession();
    }

    @Test
    void loggedInUserShouldBeStoredInSession() {
        User user = new User(
                1,
                "test@example.com",
                "password123",
                0,
                4000
        );

        userSession.setUser(user);

        assertNotNull(userSession.getUser());
        assertEquals(user, userSession.getUser());
    }

    @Test
    void logoutShouldClearCurrentUser() {
        User user = new User(
                1,
                "test@example.com",
                "password123",
                0,
                4000
        );

        userSession.setUser(user);

        userSession.clearUserSession();

        assertNull(userSession.getUser());
    }
}