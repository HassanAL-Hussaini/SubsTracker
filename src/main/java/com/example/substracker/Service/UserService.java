package com.example.substracker.Service;

import com.example.substracker.API.ApiException;
import com.example.substracker.Model.User;
import com.example.substracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(Integer id,User user){
        User oldUser = userRepository.getUserById(id);

        if(oldUser == null){
            throw new ApiException("User not found");
        }
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setMonthlySalary(user.getMonthlySalary());
        oldUser.setEmailNotificationsEnabled(user.getEmailNotificationsEnabled());
        userRepository.save(oldUser);
    }
    public void deleteUser(Integer id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }
        userRepository.delete(user);
    }
}
