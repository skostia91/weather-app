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
    public String registrationUser(@ModelAttribute("user") @Valid UserDTO userDTO,
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
        return "redirect:/weather/login";
    }//добавил redirect:weateher в адрес бо ломается прога

    @GetMapping("/login")
    public String loginPage() {
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

            int userId = userService.findByName(user.getLogin()).getId();

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
                    LocalDateTime.now().plusHours(timeLifeSession)); // срок действия сессии 4 часа

            if (sessionService.checkSessionIntoDB(userId)) {
                sessionService.deleteSessionByID(userId);
            }
            sessionService.addSession(newSession);

            return "redirect:/weather/home";
        }
    }
}