package com.pregnancy.edu.preguser;

import com.pregnancy.edu.system.consts.BloodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @NotEmpty(message = "Username is required.")
    private String username;

    @NotEmpty(message = "Password is required.")
    private String password;

    private String phoneNumber;

    private String fullName;

    private LocalDateTime dateOfBirth;

    private String avatarUrl;

    private Boolean gender;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    private String symptoms;

    private String nationality;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean verified;

    private Boolean enabled;

    private String role;
}

