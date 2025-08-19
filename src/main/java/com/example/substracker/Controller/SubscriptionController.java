package com.example.substracker.Controller;

import com.example.substracker.API.ApiResponse;
import com.example.substracker.Model.Subscription;
import com.example.substracker.Service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllSubscription(){
        return ResponseEntity.status(200).body(subscriptionService.getAllSubscription());
    }
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addSubscription(@PathVariable Integer userId,@Valid @RequestBody Subscription subscription){
        subscriptionService.addSubscription(userId,subscription);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription added successfully"));
    }
    @PutMapping("/up/{id}")
    public ResponseEntity<?> upSubscription(@PathVariable Integer id , @Valid @RequestBody Subscription subscription){
        subscriptionService.updateSubscription(id, subscription);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription updated successfully"));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSubscription(@PathVariable Integer id){
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription deleted successfully"));
    }
}
