package by.shylau.weatherapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "login", nullable = false, unique = true)
    String login;

    @Size(max = 60)
    @Column(name = "password", nullable = false)
    String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}