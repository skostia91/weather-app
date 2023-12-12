package by.shylau.weatherapp.service;

import by.shylau.weatherapp.model.Session;
import by.shylau.weatherapp.repository.SessionRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SessionService {
    private final SessionRepository sessionRepository;
    private ScheduledExecutorService executor;

    @PostConstruct
    public void init() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::checkAndInvalidateSessions, 0, 1, TimeUnit.MINUTES);
    }

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    private void checkAndInvalidateSessions() {
        long timeNow = System.currentTimeMillis();
//проверить как выводит
        log.info("time now {}", timeNow / (60 * 1000));
        List<Session> sessions = getSessions();
        for (Session session : sessions) {
            if (session.getId() != null) {
              //  System.err.println("session id = " + session.getId());
                long timeSessionDie = session.getExpiresAt()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                //System.err.println("timeSessionDie " + timeSessionDie);
                long diffInMillis = Math.abs(timeNow - timeSessionDie);
                long diffInMinutes = diffInMillis / (60 * 1000);
                log.info("Разница между значениями времени: " + diffInMinutes + " минут у сессии " + session.getId());

                if (timeNow > timeSessionDie) {
                    log.warn("Сессия протухла для пользователя: " + session.getUserId());
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
