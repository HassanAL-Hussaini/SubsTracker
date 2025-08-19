package com.example.substracker.Service;

import com.example.substracker.Model.SpendingAnalysis;
import com.example.substracker.Model.Subscription;
import com.example.substracker.Repository.SpendingAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SpendingAnalysisService {
    private final SpendingAnalysisRepository spendingAnalysisRepository;

    public List<SpendingAnalysis> getAllSpendingAnalysis(){
        return spendingAnalysisRepository.findAll();
    }
    //there is No Delete in spending Analysis

    public void AddSpendingAnalysis(){

    }




}
