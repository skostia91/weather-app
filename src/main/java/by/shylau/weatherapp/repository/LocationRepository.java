package by.shylau.weatherapp.repository;

import by.shylau.weatherapp.model.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findByUserId(Integer integer);

    @Modifying
    @Transactional
    @Query("DELETE FROM Location l WHERE l.userId = :id AND l.latitude = :lat AND l.longitude = :lon")
    void deleteLocationById(@Param("id") int idUser,
                            @Param("lat") double lat,
                            @Param("lon") double lon);
}
