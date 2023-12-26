package by.shylau.weatherapp.service;

import by.shylau.weatherapp.dto.WeatherRequestDTO;
import by.shylau.weatherapp.dto.WeatherResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiService {
    @Value(value = "${api.key}")
    String apiKey;

    @Value(value = "${api.url}")
    String apiUrl;

    public WeatherResponseDTO fetchDataFromApi(String location) throws JsonProcessingException {
        WeatherResponseDTO weatherResponseDTO = null;
        RestTemplate restTemplate = new RestTemplate();

        String result;

        try {
            result = restTemplate.getForObject(apiUrl + location + "&appid=" + apiKey + "&units=metric",
                    String.class);
        } catch (HttpClientErrorException.NotFound e) {
            return new WeatherResponseDTO();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            WeatherRequestDTO requestDTO = objectMapper.readValue(result, WeatherRequestDTO.class);

            weatherResponseDTO = new WeatherResponseDTO(
                    requestDTO.getName(),
                    requestDTO.getMain().getTemp(),
                    requestDTO.getCoord().getLon(),
                    requestDTO.getCoord().getLat()
                    );
         } catch (JsonMappingException e) {
            log.error("incorrect JSON parse ", e);
        }
        return weatherResponseDTO;
    }
}