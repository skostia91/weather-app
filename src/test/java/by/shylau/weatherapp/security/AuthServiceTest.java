package by.shylau.weatherapp.security;

import by.shylau.weatherapp.config.TestContainerConfig;
import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "skipTestContainers", matches = "false")
class AuthServiceTest extends TestContainerConfig {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Test
    void registerUser_Success() {
        User user = new User("testUser", "password123");

        boolean result = authService.saveUserInSystem(user);

        assertTrue(result);
    }

    @Test
    void registerUser_RepeatUser() {
        User user = new User("name", "password123");
        User userRepeatName = new User("name", "password12222");
        authService.saveUserInSystem(user);

        boolean result = authService.saveUserInSystem(userRepeatName);

        assertFalse(result);
    }

    @Test
    void registerUser_NoRepeatUser() {
        User user = new User("name", "password123");
        User userNoRepeatName = new User("valera", "password123");
        authService.saveUserInSystem(user);

        boolean result = authService.saveUserInSystem(userNoRepeatName);

        assertTrue(result);
    }

    @Test
    void checkUser_UserDoesNotExist() {
        User newUser = new User("newUser", "newPassword");

        boolean result = authService.checkUser(newUser);

        assertTrue(result);
    }

    @Test
    void checkPassword_ValidPassword() {
        User user = new User("name", "password123");
        User user1 = new User("name", "password123");
        authService.saveUserInSystem(user);

        boolean result = authService.checkPassword(user1);

        assertTrue(result);
    }

    @Test
    void checkPassword_NoValidPassword() {
        User user = new User("name", "password123");
        User userRepeatName = new User("name", "fake");
        authService.saveUserInSystem(user);

        boolean result = authService.checkPassword(userRepeatName);

        assertFalse(result);
    }
}
