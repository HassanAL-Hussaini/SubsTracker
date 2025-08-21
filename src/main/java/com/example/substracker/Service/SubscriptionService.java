package com.example.substracker.Service;
import com.example.substracker.API.ApiException;
import com.example.substracker.Model.SpendingAnalysis;
import com.example.substracker.Model.Subscription;
import com.example.substracker.Model.User;
import com.example.substracker.Repository.SpendingAnalysisRepository;
import com.example.substracker.Repository.SubscriptionRepository;
import com.example.substracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SpendingAnalysisService spendingAnalysisService;
    private final SpendingAnalysisRepository spendingAnalysisRepository;

    //all subscriptions for all users
    public List<Subscription> getAllSubscription(){
        return subscriptionRepository.findAll();
    }

    //all subscriptions for specific user
    public Set<Subscription> getAllSubscriptionByUserId(Integer userId){
        User user = userRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("User not found");
        }
        return user.getSubscriptions();
    }

    //create new Subscription
    public void addSubscription(Integer userId, Subscription subscription){

        User user = userRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("User not found");
        }

        if(user.getSubscriptions() == null){
            user.setSubscriptions(new HashSet<>());//create subscription List to the user
        }
        subscription.setUser(user);
        user.getSubscriptions().add(subscription);
        if(Objects.equals(subscription.getBillingPeriod(), "monthly")){
            subscription.setNextBillingDate(java.time.LocalDate.now().plusMonths(1));
        } else if(Objects.equals(subscription.getBillingPeriod(), "3month")){
            subscription.setNextBillingDate(java.time.LocalDate.now().plusMonths(3));
        } else if(Objects.equals(subscription.getBillingPeriod(), "6month")){
            subscription.setNextBillingDate(java.time.LocalDate.now().plusMonths(6));
        } else if(Objects.equals(subscription.getBillingPeriod(), "yearly")){
            subscription.setNextBillingDate(java.time.LocalDate.now().plusYears(1));
        }
        subscription.setStatus("Active");
        subscriptionRepository.save(subscription);
        //creating Spending Analysis:
        //First time >> Spending analysis creation
        if(user.getSpendingAnalysis() == null){
            SpendingAnalysis spendingAnalysis = new SpendingAnalysis();
            spendingAnalysis.setUser(user);
            user.setSpendingAnalysis(spendingAnalysis);
            spendingAnalysisRepository.save(spendingAnalysis);
        }
        spendingAnalysisService.createOrUpdateSpendingAnalysis(userId);
    }

    public void updateSubscription(Integer userId,Integer SubscriptionId,Subscription subscription){
        User user = userRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("User not found");
        }
        Set<Subscription> subscriptions = user.getSubscriptions();

        //Chack if user have this subscription ID between his subscriptions
        Subscription oldSubscription = null;
        for (Subscription sub : subscriptions){
            if(sub.getId().equals(SubscriptionId)){
                oldSubscription = sub;
            }
        }

        if(oldSubscription == null){
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
        spendingAnalysisService.createOrUpdateSpendingAnalysis(userId);
    }

    public void deleteSubscription (Integer userId,Integer subscriptionDeletedId){
        User user = userRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("User not found");
        }
        Set<Subscription> subscriptions = user.getSubscriptions();
        Subscription deletedSubscription = null;
        for (Subscription sub : subscriptions){
            if(sub.getId().equals(subscriptionDeletedId)){
                deletedSubscription = sub;
            }
        }
        if(deletedSubscription == null){
            throw new ApiException("Subscription not found");
        }
        user.getSubscriptions().remove(deletedSubscription);
        subscriptionRepository.delete(deletedSubscription);
        spendingAnalysisService.createOrUpdateSpendingAnalysis(userId);
    }

    @Scheduled(cron = "0 * * * * *")
    public void checkStatusSubscriptionExpired() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        for (Subscription subscription : subscriptions) {
            if (subscription.getNextBillingDate() == null) {
                continue; // Skip if next billing date is not set
            }
            if (!subscription.getNextBillingDate().isAfter(java.time.LocalDate.now())) {
                subscription.setStatus("Expired");
                subscriptionRepository.save(subscription);
            }
        }
    }

    public void renewSubscription(Integer userId, Integer subscriptionId,String billingPeriod) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        Set<Subscription> subscriptions = user.getSubscriptions();
        Subscription subscriptionToRenew = null;
        for (Subscription sub : subscriptions) {
            if (sub.getId().equals(subscriptionId) && sub.getStatus().equals("Expired")) {
                subscriptionToRenew = sub;
            }
        }
        if (subscriptionToRenew == null) {
            throw new ApiException("Subscription not found");
        }

        subscriptionToRenew.setBillingPeriod(billingPeriod);
        if (Objects.equals(billingPeriod, "monthly")) {
            subscriptionToRenew.setNextBillingDate(java.time.LocalDate.now().plusMonths(1));
        } else if (Objects.equals(billingPeriod, "3month")) {
            subscriptionToRenew.setNextBillingDate(java.time.LocalDate.now().plusMonths(3));
        } else if (Objects.equals(billingPeriod, "6month")) {
            subscriptionToRenew.setNextBillingDate(java.time.LocalDate.now().plusMonths(6));
        } else if (Objects.equals(billingPeriod, "yearly")) {
            subscriptionToRenew.setNextBillingDate(java.time.LocalDate.now().plusYears(1));
        }
        subscriptionToRenew.setStatus("Active");
        subscriptionRepository.save(subscriptionToRenew);
    }
}
