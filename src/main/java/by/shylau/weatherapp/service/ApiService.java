package by.shylau.weatherapp.service;

import by.shylau.weatherapp.dto.LocationDTO;
import by.shylau.weatherapp.dto.weather.WeatherLocationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiService {
    @Value(value = "${api.key}")
    String apiKey;

    @Value(value = "${api.url-location}")
    String apiUrlLocation;

    @Value(value = "${api.url-weather}")
    String apiUrlWeather;

    public LocationDTO[] fetchLocationFromApi(String location) throws JsonProcessingException {
        final int limitCities = 5;

        LocationDTO[] locationDTOS = new LocationDTO[0];
        RestTemplate restTemplate = new RestTemplate();

        String result;

        result = restTemplate.getForObject(apiUrlLocation + location +
                "&limit=" + limitCities +
                "&appid=" + apiKey + "&units=metric", String.class);
        log.info("fetchLocationFromApi {}", result);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            locationDTOS = objectMapper.readValue(result, LocationDTO[].class);
        } catch (JsonMappingException e) {
            throw new JsonParseException("incorrect JSON parse in fetchLocationFromApi: ", e);
        }
        return locationDTOS;
    }

    public WeatherLocationDTO fetchWeatherFromUser(double latitude, double longitude) throws JsonProcessingException {
        WeatherLocationDTO weatherLocationDTO = null;
        RestTemplate restTemplate = new RestTemplate();

        String result;
        result = restTemplate.getForObject(
                apiUrlWeather + "?lat=" + latitude + "&lon=" + longitude
                        + "&appid=" + apiKey + "&units=metric&lang=ru", String.class);
        log.info("fetchWeatherFromApi {}", result);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            weatherLocationDTO = objectMapper.readValue(result, WeatherLocationDTO.class);
            weatherLocationDTO.setLat(latitude);
            weatherLocationDTO.setLon(longitude);
            log.info("fetchWeatherFromApi {}", weatherLocationDTO);
        } catch (JsonMappingException e) {
            throw new JsonParseException("incorrect JSON parse in fetchWeatherFromApi", e);
        }
        return weatherLocationDTO;
    }
}
