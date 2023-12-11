package by.shylau.weatherapp.repository;

import by.shylau.weatherapp.model.Session;
import by.shylau.weatherapp.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findByUserId(int userId);
    @Transactional
    void deleteSessionByUserId(int id);
    Optional<Session> findById(String id);


}
