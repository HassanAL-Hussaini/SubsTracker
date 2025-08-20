package com.example.substracker.Controller;

import com.example.substracker.Service.SpendingAnalysisService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/spending_analysis")
public class SpendingAnalysisController {
    private final SpendingAnalysisService spendingAnalysisService;

    @PostMapping("/analyze/{userId}")
    public ResponseEntity<?> analyzeSpending(@PathVariable Integer userId) {
        return ResponseEntity.status(200)
                .body(spendingAnalysisService.getSpendingAnalysisByUserId(userId));
    }
}
