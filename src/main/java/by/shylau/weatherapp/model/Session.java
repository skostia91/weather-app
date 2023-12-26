package by.shylau.weatherapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    String id;

    @Column(name = "user_id")
    int userId;

    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;
}
