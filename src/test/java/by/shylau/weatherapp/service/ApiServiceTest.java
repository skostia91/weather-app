package by.shylau.weatherapp.service;

import by.shylau.weatherapp.config.TestContainerConfig;
import by.shylau.weatherapp.dto.WeatherResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest
public class ApiServiceTest extends TestContainerConfig {
    @Autowired
    ApiService apiService;

    @Test
    void testFetchDataFromApi_Success() throws JsonProcessingException {
        String location = "London";

        WeatherResponseDTO result = apiService.fetchDataFromApi(location);

        assertNotNull(result);
        assertEquals("London", result.getName());
        assertNotEquals("wrong-city", result.getName());
    }

    @Test
    void testFetchDataFromApi_NotSuccess() throws JsonProcessingException {
        String location = "ccc";
        String location2 = " ";

        WeatherResponseDTO result = apiService.fetchDataFromApi(location);
        WeatherResponseDTO result2 = apiService.fetchDataFromApi(location2);

        assertNotEquals(location, result.getName());
        assertNotEquals(location2, result2.getName());
    }
}
