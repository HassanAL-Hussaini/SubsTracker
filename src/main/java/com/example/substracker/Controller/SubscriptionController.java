package com.example.substracker.Controller;

import com.example.substracker.API.ApiResponse;
import com.example.substracker.Model.Subscription;
import com.example.substracker.Service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // Get all subscriptions
    @GetMapping("/get")
    public ResponseEntity<?> getAllSubscriptions() {
        return ResponseEntity.status(200).body(subscriptionService.getAllSubscription());
    }

    // Get all subscriptions for a specific user
    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getSubscriptionsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(subscriptionService.getAllSubscriptionByUserId(userId));
    }

    // Add subscription for a user
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addSubscription(@PathVariable Integer userId, @Valid @RequestBody Subscription subscription) {
        subscriptionService.addSubscription(userId, subscription);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription added successfully"));
    }

    // Update subscription
    @PutMapping("/update/{userId}/{subscriptionId}")
    public ResponseEntity<?> updateSubscription(@PathVariable Integer userId,
                                                @PathVariable Integer subscriptionId,
                                                @Valid @RequestBody Subscription subscription) {
        subscriptionService.updateSubscription(userId, subscriptionId, subscription);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription updated successfully"));
    }

    // Delete subscription
    @DeleteMapping("/delete/{userId}/{subscriptionDeletedId}")
    public ResponseEntity<?> deleteSubscription(@PathVariable Integer userId,@PathVariable Integer subscriptionDeletedId) {
        subscriptionService.deleteSubscription(userId , subscriptionDeletedId);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription deleted successfully"));
    }

    // Renew subscription
    @PutMapping("/renew/{userId}/{subscriptionId}/{billingPeriod}")
    public ResponseEntity<?> renewSubscription(@PathVariable Integer userId, @PathVariable Integer subscriptionId, @PathVariable String billingPeriod) {
        subscriptionService.renewSubscription(userId, subscriptionId, billingPeriod);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription renewed successfully"));
    }
}
