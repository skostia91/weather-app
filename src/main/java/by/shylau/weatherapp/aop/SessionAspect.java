package by.shylau.weatherapp.aop;

import by.shylau.weatherapp.service.SessionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SessionAspect {
    SessionService sessionService;

    /**
     * Методы, которые находятся в скобках, будут проверяться на наличие действующей сессии и при смерти сессии
     * функционал будет недоступен и пользователь будет redirect на страницу для ввода логина и пароля
     */
    @Pointcut("execution(* by.shylau.weatherapp.controller.LocationController.home(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.showLocationForUser(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.findLocation(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.addLocation(..)) || " +
            "execution(* by.shylau.weatherapp.controller.LocationController.deleteLocation(..))")
    public void controllerMethods() {
    }

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
            return "redirect:login-retry";
        }
    }
}