package by.shylau.weatherapp.service;

import by.shylau.weatherapp.model.Session;
import by.shylau.weatherapp.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void addSession(Session session) {
        log.info("SessionService.addSession: {}", session);
        sessionRepository.save(session);
    }

    public boolean checkSessionIntoDB(int userId) {
        Optional<Session> optional = sessionRepository.findByUserId(userId);
        log.info("SessionService.checkSessionIntoBD: {}", optional);
        return optional.isPresent();
    }

    public void deleteSessionByID(int id) {
        sessionRepository.deleteSessionByUserId(id);
    }
}
