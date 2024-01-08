package by.shylau.weatherapp.service;

import by.shylau.weatherapp.config.TestContainerConfig;
import by.shylau.weatherapp.model.Session;
import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import by.shylau.weatherapp.security.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "skipTestContainers", matches = "false")
class SessionServiceTest extends TestContainerConfig {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    AuthService authService;

    @Test
    void checkSessionIntoDB_SessionAlive() {
        User user = new User("vasil", "random");
        authService.registerUser(user);

        var idUser = userRepository.findByLogin(user.getLogin()).get().getId();
        Session session = new Session("111",
                idUser,
                LocalDateTime.now().plusMinutes(60));
        sessionService.addSession(session);

        assertTrue(sessionService.checkSessionIntoDB(idUser));
    }

    @Test
    void checkSessionIntoDB_DeadSession() {
        User user = new User("vasil", "random");
        authService.registerUser(user);

        var idUser = userRepository.findByLogin(user.getLogin()).get().getId();
        Session session = new Session("111",
                idUser,
                LocalDateTime.now().minusMinutes(60));
        sessionService.addSession(session);

        assertTrue(sessionService.checkSessionIntoDB(idUser));
    }
}
