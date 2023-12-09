package by.shylau.weatherapp.security;

import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkUser(User guest) {
        Optional<User> user = userRepository.findByLogin(guest.getLogin());

        return user.isEmpty();
    }

    public boolean checkPassword(User guest) {
        Optional<User> user = userRepository.findByLogin(guest.getLogin());

        if (BCrypt.checkpw(guest.getPassword(), user.get().getPassword())) {
            log.info("AuthRepository.authenticateUser: пароль верный {}", guest.getPassword());
            return true;
        }
        log.info("AuthRepository.authenticateUser: неверный пароль {}", guest.getPassword());
        return false;
    }

    public boolean registerUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }
}
