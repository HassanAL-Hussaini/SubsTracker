package com.example.substracker.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class AiAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer analysisId;

    @NotNull(message = "user id is required")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotNull(message = "spending analysis id is required")
    @Column(columnDefinition = "int not null")
    private Integer spendingAnalysisId;

    @NotEmpty(message = "general recommendations cannot be empty")
    @Size(min = 5, max = 1000, message = "general recommendations must be between 5 and 1000 characters")
    @Column(columnDefinition = "varchar(1000) not null")
    private String generalRecommendations;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "datetime not null")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "datetime not null")
    private LocalDateTime updatedAt;

    //TODO relation:
}
