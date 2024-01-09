package by.shylau.weatherapp.controller;

import by.shylau.weatherapp.dto.weather.WeatherLocationDTO;
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

    /**
     * В этом и некоторых других методах в аргументах метода будут присутствовать
     * @CookieValue(value = "user_id") String userId, Model model
     * которые не будут использоваться напрямую в этом методе, но нужны для работы с аоп в классе UserAspect
     * */
    @GetMapping("/home")
    public String home(@CookieValue(value = "user_id") String userId, Model model) {

        return "client/location";
    }

    @GetMapping("/show")
    public String showLocationForUser(@CookieValue(value = "user_id") String userId,
                                      Model model) throws JsonProcessingException {
        int idUser = Integer.parseInt(userId);

        List<Location> listLocation = locationService.findAllLocationForUser(idUser);
        List<WeatherLocationDTO> list = new ArrayList<>();
        if (listLocation.isEmpty()) {
            model.addAttribute("error", "Хозяин, у вас пока нет добавленных локаций");
        }

        for (Location location : listLocation) {
                var weather = apiService.fetchWeatherFromUser(
                        location.getName(),
                        location.getLatitude(),
                        location.getLongitude());
                list.add(weather);
        }
        model.addAttribute("list", list);

        return "client/location";
    }

    @PostMapping("/findLocation")
    public String findLocation(@CookieValue(value = "user_id") String userId,
                               Model model,
                               @RequestParam String location) throws JsonProcessingException {
        log.info("LocationController.findLocation: search {}", location);

        try {
            var findLocation = apiService.fetchLocationFromApi(location);
            log.info("LocationController.findLocation: found {}", findLocation);

            if (findLocation.isEmpty()) {
                model.addAttribute("error", "Хозяин, мы искали везде, но "
                        + location + " не нашли. Может вы какую-то ерунду вбили в поиск?");
            } else {
                model.addAttribute("message", "Хозяин, вот что мы нашли по запросу: " + location);
                model.addAttribute("findLocation", findLocation);
            }
        } catch (HttpClientErrorException.BadRequest e) {
            model.addAttribute("error", "Хозяин, понятно что это сложно, но " +
                    "постарайтесь ввести название локации: города/деревни, а не пустую строку вбивать");
        }
        return "client/location";
    }

    @PostMapping("/addLocation")
    public String addLocation(@CookieValue(value = "user_id") String userId,
                              @RequestParam String name,
                              @RequestParam double lat,
                              @RequestParam double lon,
                              Model model) {
        User user = userService.getUserById(Integer.parseInt(userId));

        var location = new Location(name, Integer.parseInt(userId), lat, lon);

        try {
            locationService.saveLocation(location);
        } catch (RuntimeException e) {
            log.warn("Try add repeat location, {}", name + " " + lat + " " + lon);
            model.addAttribute("error", "Хозяин, такая локация уже есть в списке");
            model.addAttribute("name", user.getLogin());
            return "client/location";
        }
        log.info("LocationController.addLocation(): {}", location + ", " + userId);
        return "redirect:/weather/show";
    }

    @PostMapping("/delete")
    public String deleteLocation(@CookieValue(value = "user_id") String userId,
                                 @RequestParam String lat,
                                 @RequestParam String lon) {
        log.info("LocationController.deleteLocation userId = {}", userId);
        log.info("LocationController.deleteLocation lat = {}", lat);
        log.info("LocationController.deleteLocation lon = {}", lon);

        locationService.deleteLocationById(Integer.parseInt(userId), Double.parseDouble(lat), Double.parseDouble(lon));

        return "redirect:/weather/show";
    }

    /**
     * Методы, которые находятся ниже(logoutJoke(), logoutSecond(), logoutThree(), logoutLast())
     * являются шуткой, которые будут пытаться остановить пользователя от выхода из системы.
     * <p>
     * Чтобы убрать этот функционал для проверки корректной работы сессий,
     * необходимо раскомментировать метод logout, который находится чуть ниже на 132-139 строчке и
     * закомментировать метод logoutJoke(), который находится на 140-143 строчках
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
    public String logoutLast(@CookieValue(value = "user_id") String userId,
                             @CookieValue(value = "session_id") String sessionId) {

        log.info("LocationController.home delete session_id {}", sessionId);
        sessionService.deleteSessionByID(Integer.parseInt(userId));
        return "client/logout-last";
    }
}