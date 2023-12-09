package by.shylau.weatherapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
