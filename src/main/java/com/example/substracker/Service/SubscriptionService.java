package com.example.substracker.Service;

import com.example.substracker.API.ApiException;
import com.example.substracker.Model.Subscription;
import com.example.substracker.Model.User;
import com.example.substracker.Repository.SubscriptionRepository;
import com.example.substracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    public List<Subscription> getAllSubscription(){
        return subscriptionRepository.findAll();
    }

    public void addSubscription(Integer userId,Subscription subscription){
        User user = userRepository.getUserById(userId);
        if (user == null){
            throw new ApiException("User not found");
        }
        subscription.setUser(user);
        subscriptionRepository.save(subscription);
    }

    public void upSubscription(Integer id , Subscription subscription){
        Subscription oldSubscription = subscriptionRepository.findSubscriptionById(id);
        if (oldSubscription == null){
            throw new ApiException("Subscription not found");
        }
        oldSubscription.setSubscriptionName(subscription.getSubscriptionName());
        oldSubscription.setCategory(subscription.getCategory());
        oldSubscription.setPrice(subscription.getPrice());
        oldSubscription.setDescription(subscription.getDescription());
        oldSubscription.setUpdatedAt(subscription.getUpdatedAt());
        oldSubscription.setCreatedAt(subscription.getCreatedAt());
        oldSubscription.setBillingPeriod(subscription.getBillingPeriod());
        oldSubscription.setNextBillingDate(subscription.getNextBillingDate());
        oldSubscription.setStatus(subscription.getStatus());
        subscriptionRepository.save(oldSubscription);

    }

    public void deleteSubscription (Integer id){
        Subscription subscription = subscriptionRepository.findSubscriptionById(id);
        if (subscription == null ){
            throw new ApiException("id not found");
        }
        subscriptionRepository.delete(subscription);
    }
}
