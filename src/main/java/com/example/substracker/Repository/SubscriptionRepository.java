package com.example.substracker.Repository;

import com.example.substracker.Model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Integer> {
    Subscription findSubscriptionById(Integer id);
    List<Subscription> findByStatus(String status);
    List<Subscription> findSubscriptionsByUserId(Integer userId);
}
