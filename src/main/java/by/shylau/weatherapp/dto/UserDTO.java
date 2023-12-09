package by.shylau.weatherapp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

//@Getter
//@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Size(min = 3, max = 30, message = "Логин должен состоять от 3 до 30 символов")
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;

    @Size(min = 3, max = 60, message = "Пароль должен состоять от 3 до 60 символов длиной")
    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;

    @Size(min = 3, max = 60, message = "Пароль должен состоять от 3 до 60 символов длиной")
    @NotBlank(message = "Пароль не должен быть пустым")
    private String repeatPassword;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}