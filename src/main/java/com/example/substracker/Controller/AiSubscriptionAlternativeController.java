package com.example.substracker.Controller;

import com.example.substracker.Model.AiSubscriptionAlternative;
import com.example.substracker.Service.AiSubscriptionAlternativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiSubscriptionAlternativeController {

    private final AiSubscriptionAlternativeService aiSubscriptionAlternativeService;

    // Generate & return an AI alternative for a given subscription (saved inside the service)
    @GetMapping("/alternative/{subscriptionId}")
    public ResponseEntity<?> generateAlternative(@PathVariable Integer subscriptionId) {
        AiSubscriptionAlternative alt =
                aiSubscriptionAlternativeService.getAiSubscriptionAlternativeByUserId(subscriptionId);
        return ResponseEntity.status(200).body(alt);
    }
}
