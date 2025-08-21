package com.example.substracker.Controller;

import com.example.substracker.API.ApiResponse;
import com.example.substracker.Model.Subscription;
import com.example.substracker.Model.User;
import com.example.substracker.Repository.SubscriptionRepository;
import com.example.substracker.Repository.UserRepository;
import com.example.substracker.Service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfService pdfService;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @GetMapping("/receipt/{userId}/{subscriptionId}")
    public ResponseEntity<?> getReceipt(@PathVariable Integer userId , @PathVariable Integer subscriptionId){
        User user = userRepository.findUserById(userId);
        if (user == null){
            return ResponseEntity.status(400).body(new ApiResponse("userId not found"));
        }

        Subscription subscription =subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription == null ){
            return ResponseEntity.status(400).body(new ApiResponse("subscriptionId not found"));
        }
        byte[] pdf = pdfService.buildSubscriptionReceipt(subscription.getId(),
                user.getName(),
                user.getEmail(),
                subscription.getSubscriptionName(),
                (subscription.getDescription() == null || subscription.getDescription().isBlank()) ? "Standard" : subscription.getDescription(),
                subscription.getBillingPeriod(),
                subscription.getPrice());

        return ResponseEntity.status(200).body(pdf);
    }
}
