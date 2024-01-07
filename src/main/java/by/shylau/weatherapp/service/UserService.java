package by.shylau.weatherapp.service;

import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
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

    /**
     * В этом методе(getUserById) настроено кэширование, для того чтобы не ходить к бд в каждом методе где надо
     * вывести имя.
     */
    @Cacheable(value = "userCache", key = "#id")
    public User getUserById(int id) {
        User user = null;
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }

    /**
     * В этом методе(evictAllCaches) настроено удаление кэша через час, для того чтобы были корректные данные
     * о погоде. Обновляется кэш каждый час (3600000 миллисекунд) через аннотацию @Sсheduled.
     * Для ручного тестирования функциональности удаление кэша нужно закомментировать строчку 54
     * и раскомментировать 55 строчку.
     */
    @CacheEvict(value = "userCache", allEntries = true)
    @Scheduled(fixedRate = 3600000)   // удаление кэша через час
//    @Scheduled(fixedRate = 15000)   // удаление кэша через 15 секунд
    public void evictAllCaches() {
        log.warn("Данные из userCache удалены. Кэш пуст");
    }
}
