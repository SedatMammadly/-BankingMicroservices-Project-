package com.sadatscode.securityservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull(message = "Name should not be null ")
    String name;
    @Email
    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    @Size(min = 8, max = 16,message = "Your password size should be min 8 and max 16 character")
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
    @Column(nullable = false)
    Boolean isEnabled;
    @CreationTimestamp
    @DateTimeFormat(pattern="dd.MM.yyyy")
    LocalDateTime createdAt;
}
