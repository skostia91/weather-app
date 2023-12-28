package by.shylau.weatherapp.repository;

import by.shylau.weatherapp.model.Session;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findByUserId(int userId);
    @Transactional
    void deleteSessionByUserId(int id);
}
