package com.example.substracker.Controller;

import com.example.substracker.API.ApiResponse;
import com.example.substracker.Model.User;
import com.example.substracker.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user){
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @Valid @RequestBody User user) {
        userService.updateUser(id, user);
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }

}
