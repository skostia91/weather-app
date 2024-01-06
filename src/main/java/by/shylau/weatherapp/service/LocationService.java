package by.shylau.weatherapp.service;

import by.shylau.weatherapp.model.Location;
import by.shylau.weatherapp.repository.LocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    public List<Location> findAllLocationForUser(int idUser) {  //тут есть ошибка? или надо чтобы путсая коллекция возвращалась?
        return locationRepository.findByUserId(idUser);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteLocationById(int idUser, double lat, double lon) {
        locationRepository.deleteLocationById(idUser, lat, lon);
    }
}