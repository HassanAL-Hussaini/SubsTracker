package com.example.substracker.Repository;

import com.example.substracker.Model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription,Integer> {
    Subscription findSubscriptionById(Integer id);
}
