package com.example.substracker.Service;

import com.example.substracker.API.ApiException;
import com.example.substracker.Model.User;
import com.example.substracker.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public void deleteUser(Integer userId){
        User userDeleted = userRepository.getUserById(userId);
        if(userDeleted == null){
            throw new ApiException("user Not found");
        }
        userRepository.delete(userDeleted);
    }

    public void updateUser(Integer userId , User user){
        User oldUser = userRepository.getUserById(userId);
        if(oldUser == null){
            throw new ApiException("user not found");
        }
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmailNotificationsEnabled(user.getEmailNotificationsEnabled());
        oldUser.setMonthlySalary(user.getMonthlySalary());
        userRepository.save(oldUser);
    }

}
