package by.shylau.weatherapp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherResponseDTO {
    String name;
    double temp;
    double lon;
    double lat;
}
