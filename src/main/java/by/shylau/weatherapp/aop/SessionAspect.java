package by.shylau.weatherapp.aop;

import by.shylau.weatherapp.service.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
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
    private final SessionService sessionService;

    @Autowired
    public SessionAspect(HttpServletResponse response, SessionService sessionService) {
        this.response = response;
        this.sessionService = sessionService;
    }


    @Pointcut("execution(* by.shylau.weatherapp.controller.LocationController.home(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.find(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.findLocation(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.addLocation(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.deleteLocation(..))")
    public void controllerMethods() {}

    //@Before("controllerMethods()")
    @Around("controllerMethods()")
    public Object checkSessionAndRedirectIfIsEmpty(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String userId = (String) args[0];
        log.info("Производится проверка сессии: " + userId);
            try {
                sessionService.checkSessionIntoDB(
                        sessionService.getSessionByUserId(Integer.parseInt(userId)).getUserId());
                log.info("checkSessionAndRedirectIfIsEmpty: Сессия действующая у " + userId);
                return joinPoint.proceed();
            } catch (RuntimeException e) {
                log.warn("Сессия отсутствует, производится редирект на страницу аутентификации");
                //return "redirect:login-retry";
                //return "weather/login-retry";
                return "redirect:login-retry";
                //response.sendRedirect("login-retry");
           }
    }
}