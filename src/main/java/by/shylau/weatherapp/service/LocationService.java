package by.shylau.weatherapp.service;

import by.shylau.weatherapp.dto.weather.WeatherLocationDTO;
import by.shylau.weatherapp.model.Location;
import by.shylau.weatherapp.repository.LocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationService {
    LocationRepository locationRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    public void saveLocation(Location location) {
        locationRepository.save(location);
    }

    public List<Location> findAllLocationForUser(int idUser) {
        return locationRepository.findByUserId(idUser);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteLocationById(int idUser, double lat, double lon) {
        locationRepository.deleteLocationById(idUser, lat, lon);
    }

    public Page<WeatherLocationDTO> findPaginated(List<WeatherLocationDTO> items, int pageNumber, int pageSize) {
        int start = pageNumber * pageSize;
        int end = Math.min((pageNumber + 1) * pageSize, items.size());
        List<WeatherLocationDTO> pageContent = items.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(pageNumber, pageSize), items.size());
    }
}