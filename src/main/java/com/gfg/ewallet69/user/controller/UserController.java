package com.gfg.ewallet69.user.controller;

import com.gfg.ewallet69.user.domain.User;
import com.gfg.ewallet69.user.service.UserService;
import com.gfg.ewallet69.user.service.resource.TransactionRequest;
import com.gfg.ewallet69.user.service.resource.UserRequest;
import com.gfg.ewallet69.user.service.resource.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("user")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest){
        userService.createUser(userRequest.toUser());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") String id){
        UserResponse userResponse=userService.getUser(id);
        return new ResponseEntity<>(userResponse,HttpStatus.OK);
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id){
        User user=userService.deleteUser(id);
        return new ResponseEntity<>(new UserResponse(user),HttpStatus.OK);
    }

    @PostMapping("/user/{user-id}/transfer")
    public ResponseEntity<?> performTransaction(@PathVariable("user-id") String userId, @RequestBody @Valid TransactionRequest transactionRequest){
        boolean success=userService.transfer(Long.valueOf(userId),transactionRequest);
        if(success)
           return new ResponseEntity<>(success,HttpStatus.OK);
        return new ResponseEntity<>(success,HttpStatus.BAD_REQUEST);
    }
}
