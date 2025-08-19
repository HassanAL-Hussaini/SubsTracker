package com.example.substracker.Repository;

import com.example.substracker.Model.ExpirationAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpirationAlertRepository extends JpaRepository<ExpirationAlert,Integer> {
    ExpirationAlert findExpirationAlertById(Integer id);
}
