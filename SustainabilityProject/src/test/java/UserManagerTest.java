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

        userDAO.addUser(
                new User(
                        1,
                        "test@example.com",
                        "password123",
                        10,
                        4000
                )
        );

        userDAO.addUser(
                new User(
                        2,
                        "second@example.com",
                        "differentPassword",
                        20,
                        4000
                )
        );
    }

    @Test
    void loginWithCorrectCredentialsShouldReturnUser() {

        User result = userManager.login(
                "test@example.com",
                "password123"
        );

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void loginWithIncorrectPasswordShouldReturnNull() {

        User result = userManager.login(
                "test@example.com",
                "wrongpassword"
        );

        assertNull(result);
    }

    @Test
    void loginWithUnknownEmailShouldReturnNull() {

        User result = userManager.login(
                "unknown@example.com",
                "password123"
        );

        assertNull(result);
    }

    @Test
    void loginWithEmptyEmailShouldReturnNull() {

        User result = userManager.login(
                "",
                "password123"
        );

        assertNull(result);
    }

    @Test
    void loginWithEmptyPasswordShouldReturnNull() {

        User result = userManager.login(
                "test@example.com",
                ""
        );

        assertNull(result);
    }

    @Test
    void loginWithEmptyCredentialsShouldReturnNull() {

        User result = userManager.login(
                "",
                ""
        );

        assertNull(result);
    }

    @Test
    void loginWithNullEmailShouldReturnNull() {

        User result = userManager.login(
                null,
                "password123"
        );

        assertNull(result);
    }

    @Test
    void loginWithNullPasswordShouldReturnNull() {

        User result = userManager.login(
                "test@example.com",
                null
        );

        assertNull(result);
    }

    @Test
    void loginWithNullCredentialsShouldReturnNull() {

        User result = userManager.login(
                null,
                null
        );

        assertNull(result);
    }

    @Test
    void loginShouldNotAcceptAnotherUsersPassword() {

        User result = userManager.login(
                "test@example.com",
                "differentPassword"
        );

        assertNull(result);
    }

    @Test
    void secondUserShouldBeAbleToLogin() {

        User result = userManager.login(
                "second@example.com",
                "differentPassword"
        );

        assertNotNull(result);
        assertEquals(2, result.getUserId());
        assertEquals("second@example.com", result.getEmail());
    }

    @Test
    void validRegistrationShouldCreateUser() {
        String result = userManager.register(
                "new@example.com",
                "password123",
                4000
        );

        assertNull(result);

        User user = userDAO.getUserByEmail("new@example.com");
        assertNotNull(user);
        assertEquals("new@example.com", user.getEmail());
        assertEquals(0, user.getUserExperience());
        assertEquals(4000, user.getPostcode());
    }

    @Test
    void duplicateEmailShouldFailRegistration() {
        assertEquals("Email already registered.", userManager.register("test@example.com", "password123", 4000));
    }

    @Test
    void blankEmailShouldFailRegistration() {
        assertEquals("Enter an email and password.", userManager.register(" ", "password123", 4000));
    }

    @Test
    void invalidEmailShouldFailRegistration() {
        assertEquals("Enter a valid email.", userManager.register("not-an-email", "password123", 4000));
    }

    @Test
    void shortPasswordShouldFailRegistration() {
        assertEquals("Password must be at least 8 characters.", userManager.register("new@example.com", "short", 4000));
    }

    @Test
    void invalidPostcodeShouldFailRegistration() {
        assertEquals("Enter a valid postcode.", userManager.register("new@example.com", "password123", 999));
    }

    @Test
    void passwordShouldBeCaseSensitive() {

        User result = userManager.login(
                "test@example.com",
                "PASSWORD123"
        );

        assertNull(result);
    }

    @Test
    void emailShouldBeCaseInsensitive() {

        User result = userManager.login(
                "TEST@EXAMPLE.COM",
                "password123"
        );

        assertNotNull(result);
        assertEquals(1, result.getUserId());
    }
}