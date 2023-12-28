package by.shylau.weatherapp.repository;

import by.shylau.weatherapp.model.Location;
import by.shylau.weatherapp.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
public class LocationRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("testdb")
                    .withUsername("root")
                    .withPassword("123");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User("oleg", "1234");
        User user2 = new User("alex", "2222");
        User user3 = new User("modest petrovich", "qwerty");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Location location1 = new Location("location-1", 1, 10, 10);
        Location location2 = new Location("location-2", 1, 20, 20);
        Location location3 = new Location("location-3", 2, 30, 30);

        locationRepository.save(location1);
        locationRepository.save(location2);
        locationRepository.save(location3);

    }

    @Transactional
    @Test
    void testGetLocationById() {
        assertEquals(3, locationRepository.count());

        assertEquals("location-1", locationRepository.getById(1).getName());
        assertEquals("location-2", locationRepository.getById(2).getName());
        assertEquals("location-3", locationRepository.getById(3).getName());

        assertEquals(20, locationRepository.getById(2).getLatitude());
        assertEquals(30, locationRepository.getById(3).getLongitude());

        assertNotEquals(2, locationRepository.getById(3).getLongitude());
        assertNotEquals(2, locationRepository.getById(3).getLatitude());
    }
}
