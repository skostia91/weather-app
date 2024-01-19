package by.shylau.weatherapp.security;

import by.shylau.weatherapp.dto.UserDTO;
import by.shylau.weatherapp.mapper.UserMapper;
import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public String registrationUser(UserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
            return "Пароль не совпадает с проверочным";
        }
        User user = userMapper.userDTOToUser(userDTO);

        if (FoolProof.defenceForFool(user.getPassword()) != null) {
            return FoolProof.defenceForFool(user.getPassword());
        }

        log.info("registration user: try register new user {}", user);

        if (!saveUserInSystem(user)) {
            log.warn("registration user: db have user with login {}", user.getLogin());

            return "Такой пользователь уже существует";
        }
        return null;
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

    public boolean saveUserInSystem(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }
}
