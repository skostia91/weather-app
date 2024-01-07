package by.shylau.weatherapp.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class WeatherLocationDTO {
    String searchName;
    String name;
    MainDTO main;
    SysDTO sys;
    double lon;
    double lat;

}
