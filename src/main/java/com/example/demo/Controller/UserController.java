package com.example.demo.Controller;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.APIResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    APIResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        APIResponse<User> response = new APIResponse<>();
        response.setCode(1000);
        response.setResult(userService.createUser(request));
        response.setMessage("User created successfully");
        return response;
    }

    @GetMapping
    APIResponse<List<User>> getAllUsers() {
        APIResponse<List<User>> response = new APIResponse<>();
        response.setCode(1000);
        response.setResult(userService.getAllUsers());
        response.setMessage("Users found");
        return response;
    }

    @GetMapping("/{UserId}")
    APIResponse<User> getUser(@PathVariable Long UserId) {
        APIResponse<User> response = new APIResponse<>();
        response.setCode(1000);
        response.setResult(userService.getUserById(UserId));
        response.setMessage("User get successfully");
        return response;
//        return userService.getUserById(UserId);
    }

    @PutMapping("/{UserId}")
     APIResponse<User> updateUser(@PathVariable Long UserId, @RequestBody @Valid UserUpdateRequest request) {
        APIResponse<User> response = new APIResponse<>();
        response.setCode(1000);
        response.setResult(userService.updateUser(UserId, request));
        response.setMessage("User updated successfully");
        return response;
    }

    @DeleteMapping("/{UserId}")
     APIResponse<String> deleteUser(@PathVariable Long UserId) {
        APIResponse<String> response = new APIResponse<>();
        if(userService.getUserById(UserId) == null) {
            response.setCode(1005);
            response.setMessage("User not exist");
        }
        else {
            userService.deleteUser(UserId);
            response.setCode(1000);
            response.setMessage("User deleted successfully");
        }
        return response;
    }

}