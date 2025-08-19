package com.example.substracker.Repository;

import com.example.substracker.Model.AiSubscriptionAlternative;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiSubscriptionAlternativeRepository extends JpaRepository<AiSubscriptionAlternative,Integer> {
    AiSubscriptionAlternative findAiSubscriptionAlternativeById(Integer id);
}
