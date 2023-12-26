package by.shylau.weatherapp.security;

import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthService authService;

    @Test
    public void testCheckUser_UserExists() {
        User existingUser = new User(1, "existingUser", "password123");
        when(userRepository.findByLogin("existingUser")).thenReturn(Optional.of(existingUser));

        boolean result = authService.checkUser(existingUser);

        assertFalse(result);
    }

    @Test
    public void testCheckUser_UserDoesNotExist() {
        User newUser = new User(1, "newUser", "newPassword");
        when(userRepository.findByLogin("newUser")).thenReturn(Optional.empty());

        boolean result = authService.checkUser(newUser);

        assertTrue(result);
    }

    @Test
    public void testCheckPasswordValidPassword() {
        User guest = new User(1, "testUser", "password123");
        User user = new User(1, "testUser", BCrypt.hashpw("password123", BCrypt.gensalt()));
        when(userRepository.findByLogin("testUser")).thenReturn(Optional.of(user));

        boolean result = authService.checkPassword(guest);

        assertTrue(result);
    }

    @Test
    public void testCheckPasswordIsNotValidPassword() {
        User guest = new User(1, "testUser", "131213123");
        User user = new User(1, "testUser", BCrypt.hashpw("password123", BCrypt.gensalt()));
        when(userRepository.findByLogin("testUser")).thenReturn(Optional.of(user));

        boolean result = authService.checkPassword(guest);

        assertFalse(result);
    }

    @Test
    public void testRegisterUserSuccess() {
        User user = new User(1, "testUser", "password123");
        when(userRepository.save(user)).thenReturn(user);

        boolean result = authService.registerUser(user);

        assertTrue(result);
    }

    @Test
    public void testRegisterUserFailure() {
        User user = new User(1, "testUser", "password123");
        when(userRepository.save(user)).thenThrow(new RuntimeException());

        boolean result = authService.registerUser(user);

        assertFalse(result);
    }
}
