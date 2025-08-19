package com.example.substracker.Repository;

import com.example.substracker.Model.SpendingAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendingAnalysisRepository extends JpaRepository<SpendingAnalysis,Integer> {
    SpendingAnalysis findSpendingAnalysisById(Integer id);

}
