package by.shylau.weatherapp.controller;

import by.shylau.weatherapp.dto.WeatherResponseDTO;
import by.shylau.weatherapp.model.Location;
import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.service.ApiService;
import by.shylau.weatherapp.service.LocationService;
import by.shylau.weatherapp.service.SessionService;
import by.shylau.weatherapp.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static by.shylau.weatherapp.service.ConverterService.convertWeatherResponseDTO;

@Controller
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/weather")
public class LocationController {
    ApiService apiService;
    UserService userService;
    LocationService locationService;
    SessionService sessionService;

    @GetMapping("/home")
    public String home(@CookieValue(value = "user_id") String userId,
                       @CookieValue(value = "session_id") String sessionId,
                       Model model) {
        log.info("LocationController.home get session_id {}", sessionId);
        log.info("LocationController.home get user_id {}", userId);

        User user = userService.getUserById(Integer.parseInt(userId));
        String loginUser = user.getLogin();

        model.addAttribute("name", loginUser);

        return "client/location";
    }

    @GetMapping("/show")
    public String find(@CookieValue(value = "user_id") String userId,
                       Model model) throws JsonProcessingException {
        int idUser = Integer.parseInt(userId);
        User user = userService.getUserById(idUser);
        model.addAttribute("name", user.getLogin());

        List<Location> listLocation = locationService.findAllLocationForUser(idUser);
        List<WeatherResponseDTO> list = new ArrayList<>();

        for (Location location : listLocation) {
            try {
                var weather = apiService.fetchDataFromApi(location.getName());
                list.add(weather);
            } catch (RuntimeException e) {
                model.addAttribute("error", "Проверьте интернет");
                return "client/location";
            }
        }
        model.addAttribute("list", list);

        return "client/location";
    }

    @PostMapping("/findLocation")
    public String findLocation(@CookieValue(value = "user_id") String userId,
                               @RequestParam String location,
                               Model model) throws JsonProcessingException {
        User user = userService.getUserById(Integer.parseInt(userId));
        model.addAttribute("name", user.getLogin());

        try {
            log.info("LocationController.findLocation: search {}", location);

            String findLocation = apiService.fetchDataFromApi(location).getName();
            log.info("LocationController.findLocation: found {}", findLocation);

            if (findLocation == null) {
                model.addAttribute("error", "Локация " + location + " не найдена");
            } else {
                model.addAttribute("successMessage", "Локация найдена");
                model.addAttribute("location", findLocation);
            }
        } catch (HttpClientErrorException.BadRequest e) {
            model.addAttribute("error", "Введите название локации: города/деревни");
            return "client/location";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Проверьте интернет");
            return "client/location";
        }
        return "client/location";
    }

    @PostMapping("/addLocation")
    public String addLocation(@CookieValue(value = "user_id") String userId,
                              @RequestParam("location") String location,
                              Model model) throws JsonProcessingException {
        WeatherResponseDTO weatherDTO;
        try {
            weatherDTO = apiService.fetchDataFromApi(location);
        } catch (RuntimeException e) {
            model.addAttribute("error", "Проверьте интернет");
            return "client/location";
        }
        User user = userService.getUserById(Integer.parseInt(userId));

        try {
            locationService.saveLocation(convertWeatherResponseDTO(weatherDTO, user.getId()));
        } catch (RuntimeException e) {
            log.warn("Try add repeat location, {}", location);
            model.addAttribute("error", "Такая локация уже есть");
            model.addAttribute("name", user.getLogin());
            return "client/location";
        }
        log.info("LocationController.addLocation(): {}", weatherDTO + ", " + userId);

        return "redirect:/weather/show";
    }

    @GetMapping("/delete/{name}")
    public String deleteLocation(@CookieValue(value = "user_id") String userId,
                                 @PathVariable("name") String name) {
        log.info("LocationController.deleteLocation userId = {}", userId);
        log.info("LocationController.deleteLocation name = {}", name);

        locationService.deleteLocationByIdAndName(Integer.parseInt(userId), name);

        return "redirect:/weather/show";
    }

    /**
     * Методы, которые находятся внизу(logoutJoke(), logoutSecond(), logoutThree(), logoutLast())
     * являются шуткой, которые будут пытаться остановить пользователя от выхода из системы.
     * <p>
     * Чтобы убрать этот функционал для проверки системы, необходимо раскомментировать метод
     * logout, который находится чуть ниже на 150-157 строчке и
     * закомментировать метод logoutJoke(), который находится на 159-162 строчках
     */

    //    @GetMapping("/logout")
//    public String logout(@CookieValue(value = "session_id") String sessionId,
//                         @CookieValue(value = "user_id") String userId) {
//
//        log.info("LocationController.home delete session_id {}", sessionId);
//        sessionService.deleteSessionByID(Integer.parseInt(userId));
//        return "auth/login";
//    }

    @GetMapping("/logout")
    public String logoutJoke() {
        return "client/logout";
    }

    @GetMapping("/logout-second")
    public String logoutSecond() {
        return "client/logout-second";
    }

    @GetMapping("/logout-three")
    public String logoutThree() {
        return "client/logout-three";
    }

    @GetMapping("/logout-last")
    public String logoutLast(@CookieValue(value = "session_id") String sessionId,
                             @CookieValue(value = "user_id") String userId) {

        log.info("LocationController.home delete session_id {}", sessionId);
        sessionService.deleteSessionByID(Integer.parseInt(userId));
        return "client/logout-last";
    }
}