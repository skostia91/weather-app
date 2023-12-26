package by.shylau.weatherapp.controller;

import by.shylau.weatherapp.dto.UserDTO;
import by.shylau.weatherapp.mapper.UserMapper;
import by.shylau.weatherapp.model.Session;
import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.security.AuthService;
import by.shylau.weatherapp.security.FoolProof;
import by.shylau.weatherapp.service.SessionService;
import by.shylau.weatherapp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/weather")
public class AuthController {
    @Value(value = "${session.timelife}")
    private int timeLifeSession;
    private final AuthService authService;
    private final SessionService sessionService;
    private final UserService userService;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Autowired
    public AuthController(AuthService authService, SessionService sessionService, UserService userService) {
        this.authService = authService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping
    public String start() {
        return "auth/guest";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "auth/regis";
    }

    @PostMapping("/register")
    public String registrationUser(HttpServletResponse response,
                                   @ModelAttribute("user") @Valid UserDTO userDTO,
                                   BindingResult bindingResult,
                                   Model model) {
        if (!userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
            model.addAttribute("errorRepeatPassword", "Пароль не совпадает с проверочным");
            return "auth/regis";
        }
        User user = userMapper.UserDTOToUser(userDTO);

        if (FoolProof.defenceForFool(user.getPassword()) != null) {
            model.addAttribute("foolProof", FoolProof.defenceForFool(user.getPassword()));
            return "auth/regis";
        }

        if (bindingResult.hasErrors()) {
            return "auth/regis";
        }
        log.info("registration user: try register new user {}", user);

        if (!authService.registerUser(user)) {
            log.warn("registration user: db have user with login {}", user.getLogin());
            model.addAttribute("errorMessage", "Такой пользователь уже существует");

            return "auth/regis";
        }

        Cookie nameNewUser = new Cookie("login_user", userDTO.getLogin());
        response.addCookie(nameNewUser);

        return "redirect:/weather/login-new";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/login-new")
    public String loginPage(@CookieValue(value = "login_user") String name, Model model) {
        model.addAttribute("message",
                "Поздравляю, " + name + " вы успешно зарегистрировались, " +
                        "теперь введите логин и пароль чтобы войти в систему");
        return "auth/login";
    }

    @GetMapping("/login-retry")
    public String loginPage(Model model) {
        model.addAttribute("errorMessage",
                "Время сессии истекло. Пожалуйста, введите логин и пароль чтобы зайти");
        return "auth/login";
    }

    //костыль, временное решение проблемы для redirect:/delete/name
    //не корректно перенаправлял при смерти сессии в аоп
    @GetMapping("/delete/login-retry")
    public String loginPageDeleteSessionDied(Model model) {
        model.addAttribute("errorMessage",
                "Время сессии истекло. Пожалуйста, введите логин и пароль чтобы зайти");
        return "auth/login";
    }

    @PostMapping("/verification")
    public String verificationUser(HttpServletResponse response,
                                   HttpServletRequest request,
                                   @ModelAttribute("user") User user,
                                   Model model) {
        log.info("verification user {}", user);

        if (authService.checkUser(user) || !authService.checkPassword(user)) {
            model.addAttribute("errorMessage", "Не правильно введены данные. " +
                    "Возможно вы не зарегистрировались в системе.");

            return "auth/login";
        } else {
            HttpSession session = request.getSession();

            int userId = userService.getUserByLogin(user.getLogin()).getId();

            String sessionId = session.getId();
            session.setAttribute("userId", userId);

            Cookie sessionCookie = new Cookie("session_id", sessionId);
            Cookie userIdCookie = new Cookie("user_id", String.valueOf(userId));

            sessionCookie.setPath("/");

            response.addCookie(sessionCookie);
            response.addCookie(userIdCookie);

            Session newSession = new Session(
                    sessionId,
                    userId,
  //                  LocalDateTime.now().plusMinutes(timeLifeSession)); //срок действия сессии 1 минута
                    LocalDateTime.now().plusHours(timeLifeSession)); //срок действия сессии 1 час

            if (sessionService.checkSessionIntoDB(userId)) {
                sessionService.deleteSessionByID(userId);
            }
            sessionService.addSession(newSession);

            return "redirect:/weather/home";
        }
    }
}