import com.example.cab302project.model.MockUserDAO;
import com.example.cab302project.model.User;
import com.example.cab302project.model.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    private MockUserDAO userDAO;
    private UserManager userManager;

    @BeforeEach
    void setUp() {
        userDAO = new MockUserDAO();
        userManager = new UserManager(userDAO);
    }

    @Test
    void loginWithCorrectCredentialsShouldReturnUser() {

        User user = new User(
                1,
                "test@example.com",
                "password123"
        );

        userDAO.addUser(user);

        User result = userManager.login(
                "test@example.com",
                "password123"
        );

        assertNotNull(result);
        assertEquals(1, result.getId());
    }
}