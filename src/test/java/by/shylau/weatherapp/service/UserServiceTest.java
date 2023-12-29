package by.shylau.weatherapp.service;

import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserByIdSuccess() {
        int userId = 1;
        User user = new User("testUser", "password123");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void testGetUserByIdNotFound() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = userService.getUserById(userId);

        assertNull(result);
    }

    @Test
    public void testGetUserByNameSuccess() {
        String userName = "testUser";
        User user = new User("testUser", "password123");
        when(userRepository.findByLogin(userName)).thenReturn(Optional.of(user));

        User result = userService.getUserByLogin(userName);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void testGetUserByNameNotFound() {
        String userName = "incorrectName";
        when(userRepository.findByLogin(userName)).thenReturn(Optional.empty());

        User result = userService.getUserByLogin(userName);

        assertNull(result);
    }
}