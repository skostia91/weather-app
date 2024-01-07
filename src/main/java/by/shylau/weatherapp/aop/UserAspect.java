package by.shylau.weatherapp.aop;

import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;

@Aspect
@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserAspect {
    UserService userService;

    /**
     * В этих методах, будет доставаться userId из кук и по этому id будет браться логин из бд.
     * Который в свою очередь будет отправлен в Model и будет выведено имя.
     */
    @Pointcut("execution(* by.shylau.weatherapp.controller.LocationController.home(..)) " + "|| " +
            "execution(* by.shylau.weatherapp.controller.LocationController.showLocationForUser(..))" + "|| " +
            "execution(* by.shylau.weatherapp.controller.LocationController.findLocation(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object processUserInformation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String userId = (String) args[0];
        Model model = (Model) args[1];

        log.info("Processing user information for user_id {}", userId);

        User user = userService.getUserById(Integer.parseInt(userId));
        String loginUser = user.getLogin();

        model.addAttribute("name", loginUser);
        return joinPoint.proceed();
    }
}
