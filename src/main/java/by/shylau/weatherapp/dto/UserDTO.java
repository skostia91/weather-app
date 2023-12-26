package by.shylau.weatherapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Size(min = 3, max = 30, message = "Логин должен состоять от 3 до 30 символов")
    @NotBlank(message = "Логин не должен быть пустым")
    String login;

    @Size(min = 3, max = 20, message = "Пароль должен состоять от 3 до 20 символов длиной")
    @NotBlank(message = "Пароль не должен быть пустым")
    String password;

    @Size(min = 3, max = 20, message = "Пароль должен состоять от 3 до 20 символов длиной")
    @NotBlank(message = "Пароль не должен быть пустым")
    String repeatPassword;
}