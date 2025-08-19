package com.example.substracker.Repository;

import com.example.substracker.Model.AiAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiAnalysisRepository extends JpaRepository<AiAnalysis,Integer> {
    AiAnalysis findAiAnalysisById(Integer id);
}
