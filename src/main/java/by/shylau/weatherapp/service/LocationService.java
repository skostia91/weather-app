package by.shylau.weatherapp.service;

import by.shylau.weatherapp.model.Location;
import by.shylau.weatherapp.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void saveLocation(Location location) {
        locationRepository.save(location);
    }

    public List<Location> findAllLocationForUser(int idUser) {  //тут есть ошибка? или надо чтобы путсая коллекция возвращалась?
        return locationRepository.findByUserId(idUser);
    }

    public void deleteLocationByIdAndName(int idUser, String nameLocation) {
        locationRepository.deleteLocationByIdAndName(idUser, nameLocation);
    }
}
