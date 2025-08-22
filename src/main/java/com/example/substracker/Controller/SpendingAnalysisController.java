package com.example.substracker.Controller;

import com.example.substracker.Service.SpendingAnalysisService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/spending_analysis")
public class SpendingAnalysisController {
    private final SpendingAnalysisService spendingAnalysisService;

    @GetMapping("/analyze/{userId}")
    public ResponseEntity<?> getSpendingAnalyzerByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(200)
                .body(spendingAnalysisService.getSpendingAnalysisByUserId(userId));
    }


}
