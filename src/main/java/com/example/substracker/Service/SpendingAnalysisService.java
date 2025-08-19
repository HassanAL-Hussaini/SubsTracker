package com.example.substracker.Service;

import com.example.substracker.API.ApiException;
import com.example.substracker.Model.SpendingAnalysis;
import com.example.substracker.Model.Subscription;
import com.example.substracker.Model.User;
import com.example.substracker.Repository.SpendingAnalysisRepository;
import com.example.substracker.Repository.SubscriptionRepository;
import com.example.substracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpendingAnalysisService {
    private final SpendingAnalysisRepository spendingAnalysisRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;


    public SpendingAnalysis getSpendingAnalysis(Integer userId){
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }

        // Check if SpendingAnalysis already exists for this user
        SpendingAnalysis spendingAnalysis = spendingAnalysisRepository.findSpendingANalysisByUserId(userId);

        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsByUserId(user.getId());
        if (subscriptions.isEmpty()) {
            throw new ApiException("No subscriptions found for user");
        }

        // If doesn't exist, create new one
        if(spendingAnalysis == null) {
            spendingAnalysis = new SpendingAnalysis();
            spendingAnalysis.setUser(user);
        }

        // Reset values before recalculating (important for existing records)
        spendingAnalysis.setDigitalSubscriptionsTotalPrice(0.0);
        spendingAnalysis.setServiceSubscriptionsTotalPrice(0.0);
        spendingAnalysis.setTotalSpendingPrice(0.0);
        spendingAnalysis.setDigitalSubscriptionsCount(0);
        spendingAnalysis.setServiceSubscriptionsCount(0);

        // Calculate values
        for(Subscription sub : subscriptions) {
            if (sub.getCategory().equals("Digital")) {
                spendingAnalysis.setDigitalSubscriptionsTotalPrice(spendingAnalysis.getDigitalSubscriptionsTotalPrice() + sub.getPrice());
                spendingAnalysis.setDigitalSubscriptionsCount(spendingAnalysis.getDigitalSubscriptionsCount() + 1);
            } else if (sub.getCategory().equals("Service")) {
                spendingAnalysis.setServiceSubscriptionsTotalPrice(spendingAnalysis.getServiceSubscriptionsTotalPrice() + sub.getPrice());
                spendingAnalysis.setServiceSubscriptionsCount(spendingAnalysis.getServiceSubscriptionsCount() + 1);
            }
            spendingAnalysis.setTotalSpendingPrice(spendingAnalysis.getTotalSpendingPrice() + sub.getPrice());
        }

        spendingAnalysis.setAverageSubscriptionCost(spendingAnalysis.getTotalSpendingPrice() / subscriptions.size());
        spendingAnalysis.setSpendingToIncomeRatio((spendingAnalysis.getTotalSpendingPrice() / user.getMonthlySalary()) * 100);
        spendingAnalysis.setTotalSubscriptionsCount(subscriptions.size());

        spendingAnalysisRepository.save(spendingAnalysis);
        return spendingAnalysis;
    }
}
