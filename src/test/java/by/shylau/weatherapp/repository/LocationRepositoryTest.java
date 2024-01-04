package by.shylau.weatherapp.repository;

import by.shylau.weatherapp.config.TestContainerConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(named = "skipTestContainers", matches = "false")
public class LocationRepositoryTest extends TestContainerConfig {
    @Autowired
    private LocationRepository locationRepository;

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
