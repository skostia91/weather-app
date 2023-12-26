package by.shylau.weatherapp.service;

import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;

    public User getUserByLogin(String name) {
        User user = null;
        Optional<User> optional = userRepository.findByLogin(name);
        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }

    public User getUserById(int id) {
        User user = null;
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }
}
