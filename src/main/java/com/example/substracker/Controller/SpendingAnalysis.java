package com.example.substracker.Controller;

import com.example.substracker.Service.SpendingAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/spending-analysis")
@RequiredArgsConstructor
public class SpendingAnalysis {

    private final SpendingAnalysisService spendingAnalysisService;

    @PostMapping("/analyze/{userId}")
    public ResponseEntity<?> analyzeSpending(@PathVariable Integer userId) {
        return ResponseEntity.status(200)
                .body(spendingAnalysisService.getSpendingAnalysis(userId));
    }
}
