package com.example.substracker.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email cannot be empty")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String name;

    @NotNull(message = "Monthly salary is required")
    @Positive(message = "Monthly salary must be greater than 0")
    @Column(nullable = false)
    private Double monthlySalary;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @NotNull
    @NotNull(message = "Email notifications flag is required")
    @Column(nullable = false)
    private Boolean emailNotificationsEnabled;

    //TODO Relation: One to Many With Subscription.
    //TODO Relation: One to One with Spending Analysis.
}
