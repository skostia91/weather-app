package by.shylau.weatherapp.mapper;


import by.shylau.weatherapp.dto.WeatherResponseDTO;
import by.shylau.weatherapp.model.Location;

public class MapperService {

    public static Location convertWeatherResponseDTOToLocation(WeatherResponseDTO dto, int userId) {
        return new Location(
                dto.getName(),
                userId,
                dto.getLat(),
                dto.getLon()
                );
    }
}
