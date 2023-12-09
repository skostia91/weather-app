package by.shylau.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class WeatherRequestDTO {

    private MainDTO main;
    private CoordinatesDTO coord;
    private String name;

}
