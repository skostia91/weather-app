package by.shylau.weatherapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login", nullable = false, unique = true)
    @Size(min = 3, max = 30, message = "Логин должен состоять от 3 до 30 символов")
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;

    @Size(min = 3, max = 60, message = "Пароль должен состоять от 3 до 60 символов длиной")
    @NotBlank(message = "Пароль не должен быть пустым")
    @Column(name = "password", nullable = false)
    private String password;
}