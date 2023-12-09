package by.shylau.weatherapp.repository;

import by.shylau.weatherapp.model.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findByUserId(Integer integer);

    @Modifying
    @Transactional
    @Query("DELETE FROM Location l WHERE l.userId = :id AND l.name = :name")
    void deleteLocationByIdAndName(@Param("id") int idUser, @Param("name") String name);


}
