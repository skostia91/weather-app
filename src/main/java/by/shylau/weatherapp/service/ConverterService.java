package by.shylau.weatherapp.service;


import by.shylau.weatherapp.dto.WeatherResponseDTO;
import by.shylau.weatherapp.model.Location;

public class ConverterService {

    public static Location convertWeatherResponseDTOToLocation(WeatherResponseDTO dto, int userId) {
        return new Location(
                dto.getName(),
                userId,
                dto.getLat(),
                dto.getLon()
                );
    }
}
