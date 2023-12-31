package by.shylau.weatherapp.service;

import by.shylau.weatherapp.model.Session;
import by.shylau.weatherapp.repository.SessionRepository;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionService {
    SessionRepository sessionRepository;
    @NonFinal ScheduledExecutorService executor;

    @PostConstruct
    public void init() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::checkAndInvalidateSessions, 0, 1, TimeUnit.MINUTES);
    }

    public void checkAndInvalidateSessions() {
        long timeNow = System.currentTimeMillis();
        log.info("time now {}", timeNow / (60 * 1000));
        List<Session> sessions = getSessions();
        for (Session session : sessions) {
            if (session.getId() != null) {
                long timeSessionDie = session.getExpiresAt()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                long diffInMillis = Math.abs(timeNow - timeSessionDie);
                long diffInMinutes = diffInMillis / (60 * 1000);
                log.info("Время жизни осталось у сессии " + session.getId() + ": " + diffInMinutes + " минут");

                if (timeNow > timeSessionDie) {
                    log.error("Сессия умерла у пользователя: " + session.getUserId());
                    sessionRepository.deleteSessionByUserId(session.getUserId());
                } else {
                    log.info("Сессия действительна для пользователя: " + session.getUserId());
                }
            }
        }
    }

    public void addSession(Session session) {
        log.info("SessionService.addSession: {}", session);
        sessionRepository.save(session);
    }

    public Session getSessionByUserId(int userId) {
        Session session = null;
        Optional<Session> optional = sessionRepository.findByUserId(userId);
        if (optional.isPresent()) {
            session = optional.get();
        }
        return session;
    }

    public List<Session> getSessions() {
        return sessionRepository.findAll();
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
