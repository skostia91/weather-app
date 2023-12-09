package by.shylau.weatherapp.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class WeatherResponseDTO {
    private String name;
    private double temp;
    private double lon;
    private double lat;
}
