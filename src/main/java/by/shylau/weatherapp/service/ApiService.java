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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
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

        result = restTemplate.getForObject(apiUrlLocation + "?q=" + location +
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

    /**
     * В этом методе(fetchWeatherFromUser) настроено кэширование, для того чтобы не ходить к апи каждый раз для
     * популярных локаций.
     */
    @Cacheable(value = "weatherCache", key = "#latitude + '_' + #longitude")
    public WeatherLocationDTO fetchWeatherFromUser(double latitude, double longitude) throws JsonProcessingException {
        WeatherLocationDTO weatherLocationDTO = null;
        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(
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

    /**
     * В этом методе(evictAllCaches) настроено удаление кэша через час, для того чтобы были корректные данные
     * о погоде. Обновляется кэш каждый час (3600000 миллисекунд) через аннотацию @Sсheduled.
     * Для ручного тестирования функциональности удаление кэша нужно закомментировать строчку 88
     * и раскомментировать 89 строчку.
     */
    @CacheEvict(value = "weatherCache", allEntries = true)
    @Scheduled(fixedRate = 3600000)   // удаление кэша через час
//    @Scheduled(fixedRate = 15000)   // удаление кэша через 15 секунд
    public void evictAllCaches() {
        log.error("Кэш удалён");
    }
}
