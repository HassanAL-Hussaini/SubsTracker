package com.example.substracker.Service;

import com.example.substracker.API.ApiException;
import com.example.substracker.DTO.AiAnalysisDTOOut;
import com.example.substracker.DTO.SpendingAnalysisDTOOut;
import com.example.substracker.Model.AiAnalysis;
import com.example.substracker.Model.SpendingAnalysis;
import com.example.substracker.Model.Subscription;
import com.example.substracker.Model.User;
import com.example.substracker.Repository.SpendingAnalysisRepository;
import com.example.substracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SpendingAnalysisService {
    private final SpendingAnalysisRepository spendingAnalysisRepository;
    private final UserRepository userRepository;
    private final AiAnalysisService aiAnalysisService;

    //there is No Delete in spending Analysis

    public SpendingAnalysisDTOOut getSpendingAnalysisByUserId(Integer userId){
        User user = userRepository.findUserById(userId);
        if(user == null) throw new ApiException("User not found");
        SpendingAnalysis sa = user.getSpendingAnalysis();
        if(sa == null) throw new ApiException("User spending analysis not found (no subscriptions yet)");

        AiAnalysis ai = sa.getAiAnalysis();
        AiAnalysisDTOOut aiDto = (ai != null) ? new AiAnalysisDTOOut(ai.getGeneralRecommendations()) : null;

        return new SpendingAnalysisDTOOut(
                sa.getDigitalSubscriptionsTotalPrice(),
                sa.getServiceSubscriptionsTotalPrice(),
                sa.getTotalSpendingPrice(),
                sa.getAverageSubscriptionCost(),
                sa.getSpendingToIncomeRatio(),
                sa.getTotalSubscriptionsCount(),
                sa.getDigitalSubscriptionsCount(),
                sa.getServiceSubscriptionsCount(),
                aiDto
        );
    }

    public SpendingAnalysisDTOOut getSpendingAnalysisDTOOutByUserId(Integer userId){
        User user = userRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("User not found");
        }
        if(user.getSpendingAnalysis() == null){
            throw new ApiException("User spending analysis not found because he have not subscriptions yet");
        }

        SpendingAnalysis spendingAnalysis = user.getSpendingAnalysis();

        // Convert AiAnalysis to DTO
        AiAnalysisDTOOut aiAnalysisDTOOut = null;
        AiAnalysis aiAnalysis = spendingAnalysis.getAiAnalysis();
        if(aiAnalysis != null) {
            aiAnalysisDTOOut = new AiAnalysisDTOOut(
                    aiAnalysis.getGeneralRecommendations()
            );
        }

        return new SpendingAnalysisDTOOut(
                spendingAnalysis.getDigitalSubscriptionsTotalPrice(),
                spendingAnalysis.getServiceSubscriptionsTotalPrice(),
                spendingAnalysis.getTotalSpendingPrice(),
                spendingAnalysis.getAverageSubscriptionCost(),
                spendingAnalysis.getSpendingToIncomeRatio(),
                spendingAnalysis.getTotalSubscriptionsCount(),
                spendingAnalysis.getDigitalSubscriptionsCount(),
                spendingAnalysis.getServiceSubscriptionsCount(),
                aiAnalysisDTOOut
        );
    }

    public List<SpendingAnalysisDTOOut> getAllSpendingAnalysisDTOOut(){
        ArrayList<SpendingAnalysisDTOOut> spendingAnalysisDTOOuts = new ArrayList<>();

        for(SpendingAnalysis spendingAnalysis : spendingAnalysisRepository.findAll()) {
            // Convert AiAnalysis to DTO
            AiAnalysisDTOOut aiAnalysisDTOOut = null;
            AiAnalysis aiAnalysis = spendingAnalysis.getAiAnalysis();
            if(aiAnalysis != null) {
                aiAnalysisDTOOut = new AiAnalysisDTOOut(
                        aiAnalysis.getGeneralRecommendations()
                );
            }

            SpendingAnalysisDTOOut spendingAnalysisDTOOut = new SpendingAnalysisDTOOut(
                    spendingAnalysis.getDigitalSubscriptionsTotalPrice(),
                    spendingAnalysis.getServiceSubscriptionsTotalPrice(),
                    spendingAnalysis.getTotalSpendingPrice(),
                    spendingAnalysis.getAverageSubscriptionCost(),
                    spendingAnalysis.getSpendingToIncomeRatio(),
                    spendingAnalysis.getTotalSubscriptionsCount(),
                    spendingAnalysis.getDigitalSubscriptionsCount(),
                    spendingAnalysis.getServiceSubscriptionsCount(),
                    aiAnalysisDTOOut
            );
            spendingAnalysisDTOOuts.add(spendingAnalysisDTOOut);
        }
        return spendingAnalysisDTOOuts;
    }

    public void createOrUpdateSpendingAnalysis(Integer userId){
        User user = userRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("user not found");
        }

        Set<Subscription> subscriptions = user.getSubscriptions();
        if(subscriptions == null || subscriptions.isEmpty()){
            throw new  ApiException("User does not have subscriptions yet");
        }

        Set<Subscription> activeSubscriptions = new HashSet<>();
        for(Subscription subscription : subscriptions){
            if(subscription.getStatus().equals("Active"))
                activeSubscriptions.add(subscription);
        }

        if(activeSubscriptions.isEmpty()){
            throw new ApiException("you dont have Active subscriptions to analyse it");
        }

        SpendingAnalysis spendingAnalysis = user.getSpendingAnalysis();
        if(spendingAnalysis == null){
            throw new ApiException("spendingAnalysis not found");
        }

        // Reset values before recalculating (important for existing records)
        spendingAnalysis.setDigitalSubscriptionsTotalPrice(0.0);
        spendingAnalysis.setServiceSubscriptionsTotalPrice(0.0);
        spendingAnalysis.setTotalSpendingPrice(0.0);
        spendingAnalysis.setDigitalSubscriptionsCount(0);
        spendingAnalysis.setServiceSubscriptionsCount(0);

        for(Subscription subscription : activeSubscriptions){
            if(subscription.getCategory().equals("Digital")){
                spendingAnalysis.setDigitalSubscriptionsTotalPrice(spendingAnalysis.getDigitalSubscriptionsTotalPrice() + subscription.getPrice());
                spendingAnalysis.setDigitalSubscriptionsCount(spendingAnalysis.getDigitalSubscriptionsCount() + 1);
            }else if(subscription.getCategory().equals("Service")){
                spendingAnalysis.setServiceSubscriptionsTotalPrice(spendingAnalysis.getServiceSubscriptionsTotalPrice() + subscription.getPrice());
                spendingAnalysis.setServiceSubscriptionsCount(spendingAnalysis.getServiceSubscriptionsCount() + 1);
            }
            spendingAnalysis.setTotalSpendingPrice(spendingAnalysis.getTotalSpendingPrice() + subscription.getPrice());
        }

        spendingAnalysis.setAverageSubscriptionCost(spendingAnalysis.getTotalSpendingPrice() / activeSubscriptions.size());
        spendingAnalysis.setTotalSubscriptionsCount(activeSubscriptions.size());
        spendingAnalysis.setSpendingToIncomeRatio((spendingAnalysis.getTotalSpendingPrice() / user.getMonthlySalary()) * 100);

        if(user.getIsSubscribed() == true){
        //AI analysis:
            if(spendingAnalysis.getAiAnalysis() == null){
                AiAnalysis aiAnalysis = new AiAnalysis();
                spendingAnalysis.setAiAnalysis(aiAnalysis);
                aiAnalysis.setSpendingAnalysis(spendingAnalysis);
            }
            aiAnalysisService.addOrUpdateRecommendation(userId , spendingAnalysis.getId());
            spendingAnalysis.setUser(user);
            spendingAnalysisRepository.save(spendingAnalysis);
            return;//end of the method if the user Subscribed
        }
        //if the user is not Subscribed
        spendingAnalysis.setAiAnalysis(null);

        spendingAnalysis.setUser(user);
        spendingAnalysisRepository.save(spendingAnalysis);

    }
}