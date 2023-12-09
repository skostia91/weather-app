package by.shylau.weatherapp.service;

import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByName(String name) {
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
