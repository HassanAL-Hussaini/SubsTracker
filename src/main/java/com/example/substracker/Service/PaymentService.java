package com.example.substracker.Service;

import com.example.substracker.API.ApiException;
import com.example.substracker.Model.PaymentRequest;
import com.example.substracker.Model.User;
import com.example.substracker.Repository.PaymentRequestRepository;
import com.example.substracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentService {


    private final PaymentRequestRepository paymentRequestRepository;
    @Value("${moyasar.api.key}")
    private String apiKey;

    private static final String MOYASAR_API_URL = "https://api.moyasar.com/v1/payments/";
    private final UserRepository userRepository;

    public void addPaymentCard(Integer userId,PaymentRequest paymentRequest){
        User user = userRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("user not found");
        }
        paymentRequest.setUser(user);
        paymentRequestRepository.save(paymentRequest);
        user.setPaymentRequest(paymentRequest);
        userRepository.save(user);

    }

    //Pay 30$ and subscribe forever in AI subsTracker Tool
    public ResponseEntity<?> processPayment(Integer userId){
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("user not found");
        }
        if (user.getPaymentRequest() == null) {
            throw new ApiException("user card Info is Empty (Add Card First)");
        }

        if(user.getPaymentRequest().getAmount() < 30){
            throw new ApiException("you dont have enough Money 30$ Required");
        }

        if(user.getIsSubscribed()){
            throw new ApiException("you are already Subscribed Before");
        }

        String url = "https://api.moyasar.com/v1/payments/";
        String callbackUrl = "http://localhost:8081/api/v1/payment/card";

        String requestBody = String.format(
                "source[type]=card" +
                        "&source[name]=%s" +
                        "&source[number]=%s" +
                        "&source[cvc]=%s" +
                        "&source[month]=%s" +
                        "&source[year]=%s" +
                        "&amount=%d" +
                        "&currency=%s" +
                        "&callback_url=%s",
                user.getPaymentRequest().getName(),
                user.getPaymentRequest().getNumber(),
                user.getPaymentRequest().getCvc(),
                user.getPaymentRequest().getMonth(),
                user.getPaymentRequest().getYear(),
                (int) (user.getPaymentRequest().getAmount() * 100), // halalas
                user.getPaymentRequest().getCurrency(),
                callbackUrl
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, ""); // Moyasar: key as username, empty password
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // === NEW: parse JSON and persist only what you need ===
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(response.getBody());

            String paymentId = root.path("id").asText(null);
            String transactionUrl = root.path("source").path("transaction_url").asText(null);

            if (paymentId == null || transactionUrl == null) {
                throw new ApiException("Payment response missing required fields (id / transaction_url)");
            }

            // Save into your PaymentRequest (user can't edit these because of @JsonIgnore)
            PaymentRequest pr = user.getPaymentRequest();
            pr.setPaymentUserId(paymentId);
            pr.setRedirectToCompletePayment(transactionUrl);
            paymentRequestRepository.save(pr);

            // Return a minimal JSON response
            java.util.Map<String, String> result = new java.util.HashMap<>();
            result.put("payment_user_id", paymentId);
            result.put("transaction_url", transactionUrl);

            // keep upstream status (e.g., 201 CREATED) but send trimmed body
            return ResponseEntity.status(response.getStatusCode()).body(result);

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new ApiException("Failed to parse payment response JSON");
        }
    }

    // this will change the user.isSubscription from false to true if user paid
    public String checkPayment(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("user not found");
        }
        if (user.getPaymentRequest() == null) {
            throw new ApiException("user dose not have card and the payment Process is Not Started yet");
        }
        if (user.getPaymentRequest().getPaymentUserId() == null) {
            throw new ApiException("Pay first before Changing the status");
        }

        if(user.getIsSubscribed()){
            throw new ApiException("you are subscriber");
        }

        //take Money :
        user.getPaymentRequest().setAmount(user.getPaymentRequest().getAmount() - 30);



        String paymentId = user.getPaymentRequest().getPaymentUserId();

        // Prepare headers with authentication
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                MOYASAR_API_URL + paymentId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        // Parse JSON and check status
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(response.getBody());

            String status = root.path("status").asText("");  // e.g. "paid"
            boolean paid = "paid".equalsIgnoreCase(status);

            // Update user flag
            user.setIsSubscribed(paid);
            userRepository.save(user);

            if (paid) {
                return "successfully subscribe";
            } else {
                // transaction_url غالباً يصير null بعد الدفع، نرجع الموجود المخزَّن عندنا
                String txUrl = root.path("source").path("transaction_url").asText(null);
                if (txUrl == null || "null".equals(txUrl)) {
                    txUrl = user.getPaymentRequest().getRedirectToCompletePayment();
                }
                return "submit the request from this Url: " + txUrl;
            }
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new ApiException("Failed to parse payment JSON");
        }
    }


    public PaymentRequest getPaymentCardByUserId(Integer userId){
        User user = userRepository.findUserById(userId);
        if(user ==  null){
            throw new ApiException("user not found");
        }
        if(user.getPaymentRequest() == null){
            throw new ApiException("user dose not add his Payment Card");
        }
        return user.getPaymentRequest();
    }




}