package by.shylau.weatherapp.service;

import by.shylau.weatherapp.config.TestContainerConfig;
import by.shylau.weatherapp.model.Location;
import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.LocationRepository;
import by.shylau.weatherapp.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertThrows;

@EnabledIfSystemProperty(named = "skipTestContainers", matches = "false")
class LocationServiceTest extends TestContainerConfig {
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LocationService locationService;

    @Autowired
    UserRepository userRepository;

    @Test
    void save_FailSaveRepeatNameLatitudeLongitude() {
        User user1 = new User("oleg", "1234");

        userRepository.save(user1);

        Location location1 = new Location("Гомель", 1, 11, 11);
        Location location2 = new Location("Гомель", 1, 11, 11);
        locationRepository.save(location1);

        assertThrows(Exception.class, () -> locationRepository.save(location2));
    }
}
