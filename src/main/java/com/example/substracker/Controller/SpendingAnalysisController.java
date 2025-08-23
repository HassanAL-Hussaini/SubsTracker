package com.example.substracker.Controller;

import com.example.substracker.DTO.SpendingAnalysisDTOOut;
import com.example.substracker.Service.SpendingAnalysisService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/spending-analysis")
public class SpendingAnalysisController {
    private final SpendingAnalysisService spendingAnalysisService;

    @GetMapping("/analyze/{userId}")
    public ResponseEntity<?> getSpendingAnalyzerByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(200)
                .body(spendingAnalysisService.getSpendingAnalysisByUserId(userId));
    }

    @GetMapping("/analyze/{userId}dto")
    public ResponseEntity<SpendingAnalysisDTOOut> getSpendingAnalysisByUserId(@PathVariable Integer userId) {
        SpendingAnalysisDTOOut spendingAnalysisDTOOut = spendingAnalysisService.getSpendingAnalysisDTOOutByUserId(userId);
        return ResponseEntity.ok(spendingAnalysisDTOOut);
    }

    @GetMapping("/get-all-spending")
    public ResponseEntity<List<SpendingAnalysisDTOOut>> getAllSpendingAnalysis() {
        List<SpendingAnalysisDTOOut> allSpendingAnalysis = spendingAnalysisService.getAllSpendingAnalysisDTOOut();
        return ResponseEntity.ok(allSpendingAnalysis);
    }

}
