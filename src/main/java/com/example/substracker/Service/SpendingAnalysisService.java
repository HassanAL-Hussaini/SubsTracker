package com.example.substracker.Service;

import com.example.substracker.API.ApiException;
import com.example.substracker.Model.AiAnalysis;
import com.example.substracker.Model.SpendingAnalysis;
import com.example.substracker.Model.Subscription;
import com.example.substracker.Model.User;
import com.example.substracker.Repository.SpendingAnalysisRepository;
import com.example.substracker.Repository.SubscriptionRepository;
import com.example.substracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SpendingAnalysisService {
    private final SpendingAnalysisRepository spendingAnalysisRepository;
    private final UserRepository userRepository;
    private final AiAnalysisService aiAnalysisService;


    //there is No Delete in spending Analysis


//    public List<SpendingAnalysis> getAllSpendingAnalysis(){
//        return spendingAnalysisRepository.findAll();
//    }

    public SpendingAnalysis getSpendingAnalysisByUserId(Integer userId){
        User user = userRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("User not found");
        }
        if(user.getSpendingAnalysis() == null){
            throw new ApiException("User spending analysis not found because he have not subscriptions yet");
        }
        return spendingAnalysisRepository.findSpendingAnalysisById(user.getSpendingAnalysis().getId());
    }


    public void createOrUpdateSpendingAnalysis(Integer userId){
        User user = userRepository.findUserById(userId);
        Set<Subscription> subscriptions = user.getSubscriptions();
        SpendingAnalysis spendingAnalysis = user.getSpendingAnalysis();
        if(user == null){
            throw new ApiException("user not found");
        }

        if(subscriptions == null){
            throw new ApiException("subscriptions not found");
        }

        if(spendingAnalysis == null){
            throw new ApiException("spendingAnalysis not found");
        }

        // Reset values before recalculating (important for existing records)
        spendingAnalysis.setDigitalSubscriptionsTotalPrice(0.0);
        spendingAnalysis.setServiceSubscriptionsTotalPrice(0.0);
        spendingAnalysis.setTotalSpendingPrice(0.0);
        spendingAnalysis.setDigitalSubscriptionsCount(0);
        spendingAnalysis.setServiceSubscriptionsCount(0);

        for(Subscription subscription : subscriptions){
            if(subscription.getCategory().equals("Digital")){
                spendingAnalysis.setDigitalSubscriptionsTotalPrice(spendingAnalysis.getDigitalSubscriptionsTotalPrice() + subscription.getPrice());
                spendingAnalysis.setDigitalSubscriptionsCount(spendingAnalysis.getDigitalSubscriptionsCount() + 1);
            }else if(subscription.getCategory().equals("Service")){
                spendingAnalysis.setServiceSubscriptionsTotalPrice(spendingAnalysis.getServiceSubscriptionsTotalPrice() + subscription.getPrice());
                spendingAnalysis.setServiceSubscriptionsCount(spendingAnalysis.getServiceSubscriptionsCount() + 1);
            }
            spendingAnalysis.setTotalSpendingPrice(spendingAnalysis.getTotalSpendingPrice() + subscription.getPrice());
        }

        spendingAnalysis.setAverageSubscriptionCost(spendingAnalysis.getTotalSpendingPrice() / subscriptions.size());
        spendingAnalysis.setTotalSubscriptionsCount(subscriptions.size());
        spendingAnalysis.setSpendingToIncomeRatio((spendingAnalysis.getTotalSpendingPrice() / user.getMonthlySalary()) * 100);

        //TODO AI analysis:
        if(spendingAnalysis.getAiAnalysis() == null){
            AiAnalysis aiAnalysis = new AiAnalysis();
            spendingAnalysis.setAiAnalysis(aiAnalysis);
            aiAnalysis.setSpendingAnalysis(spendingAnalysis);
        }
        aiAnalysisService.addOrUpdateRecommendation(userId , spendingAnalysis.getId());
        spendingAnalysis.setUser(user);
        spendingAnalysisRepository.save(spendingAnalysis);
    }






}
