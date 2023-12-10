package by.shylau.weatherapp.aop;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Aspect
@Component
@Slf4j
public class SessionAspect {
    private final HttpServletResponse response;

    @Autowired
    public SessionAspect(HttpServletResponse response) {
        this.response = response;
    }

    @Pointcut("execution(* by.shylau.weatherapp.controller.LocationController.home(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.find(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.findLocation(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.addLocation(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.deleteLocation(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void checkSessionAndRedirect(JoinPoint joinPoint) throws IOException {
        Object[] args = joinPoint.getArgs();
        String sessionId = (String) args[0];
        log.info("Производится проверка сессии: " + sessionId);
        if (sessionId.equals("")) {
            log.warn("Сессия отсутствует, производится редирект на страницу аутентификации");
            response.sendRedirect("login-retry");
        }
        log.info("Сессия действующая");
    }
}