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

    //что мы тут делали???
//    public Location getLocationById(int id) {
//        Location location = null;
//        Optional<Location> optional = locationRepository.findById(id);
//        if (optional.isPresent()) {
//            location = optional.get();
//        }
//        return location;
//    }

//    public Location getLocationByName(String name) {
//        System.err.println(locationRepository.findByName(name));
//        return locationRepository.findByName(name);
//    }

    public void saveLocation(Location location) {
        locationRepository.save(location);
    }

    public List<Location> findAllLocationForUser(int idUser) {  //тут есть ошибка? или надо чтобы путсая коллекция возвращалась?
        return locationRepository.findByUserId(idUser);
    }

    public void deleteLocationByIdAndName(int idUser, String nameLocation) {
        //System.err.println("просмотр ======= " + idUser + "  +++ " + nameLocation);
        locationRepository.deleteLocationByIdAndName(idUser, nameLocation);
    }


}
