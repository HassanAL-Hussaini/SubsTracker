package com.example.substracker.Repository;

import com.example.substracker.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserById(Integer id);
}
