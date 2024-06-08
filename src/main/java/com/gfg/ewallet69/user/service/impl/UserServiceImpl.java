package com.gfg.ewallet69.user.service.impl;

import com.gfg.ewallet69.user.domain.User;
import com.gfg.ewallet69.user.exception.UserException;
import com.gfg.ewallet69.user.feingclient.TransactionClient;
import com.gfg.ewallet69.user.repository.UserRepository;
import com.gfg.ewallet69.user.service.UserService;
import com.gfg.ewallet69.user.service.resource.TransactionRequest;
import com.gfg.ewallet69.user.service.resource.UserRequest;
import com.gfg.ewallet69.user.service.resource.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Value("${kafka.topic.user-created}")
    private String USER_CREATED_TOPIC;

    @Value("${kafka.topic.user-deleted}")
    private String USER_DELETED_TOPIC;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TransactionClient client;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public void createUser(User user) {
        //check if the user is valid
        //check if username exists
        Optional<User> userOptional=userRepository.findByName(user.getName());
        if(userOptional.isPresent()){
            throw new UserException("EWALLET_USER_EXISTS_EXCEPTION","User already exists");
        }
        //encode the password before storing
        user.setPassword(encoder.encode(user.getPassword()));

        //save the user to DB.
        userRepository.save(user);

        //create a user created event
        kafkaTemplate.send(USER_CREATED_TOPIC,String.valueOf(user.getId()));
    }

    @Override
    public UserResponse getUser(String id) {
        Optional<User> userOptional=userRepository.findById(Long.valueOf(id));
        User user = userOptional.orElseThrow(() -> new UserException("EWALLET_USER_NOT_FOUND_EXCEPTION","User not found"));
        return new UserResponse(user);
    }

    @Override
    public User deleteUser(String id) {
        Optional<User> userOptional=userRepository.findById(Long.valueOf(id));
        if(userOptional.isEmpty()){
            throw new UserException("EWALLET_USER_NOT_FOUND_EXCEPTION","User not found");
        }
        userRepository.deleteById(Long.valueOf(id));
        kafkaTemplate.send(USER_DELETED_TOPIC,id);
        return userOptional.get();
    }

    @Override
    public User updateUser(UserRequest userRequest, String id) {
        Optional<User> userOptional=userRepository.findById(Long.valueOf(id));
        if(userOptional.isEmpty()){
            throw  new UserException("EWALLET_USER_NOT_FOUND_EXCEPTION","User not found");
        }
        validateChange(userOptional.get(),userRequest);
        User user=userOptional.get();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        return userRepository.save(user);
    }

    private void validateChange(User user, UserRequest userRequest) {
        if(user.getName().equals(userRequest.getName()) && user.getEmail().equals(userRequest.getEmail()) && user.getPassword().equals(userRequest.getPassword())){
            throw new UserException("EWALLET_USER_NOT_CHANGED_EXCEPTION","User not changed");
        }
    }

    @Override
    public boolean transfer(Long userId, TransactionRequest request) {
        Optional<User> senderOptional=userRepository.findById(userId);
        if(senderOptional.isEmpty()){
            throw new UserException("EWALLET_USER_NOT_FOUND_EXCEPTION","User not found");
        }
        Optional<User> receiverOptional=userRepository.findById(request.getReceiverId());
        if(receiverOptional.isEmpty()) {
            throw new UserException("EWALLET_RECEIVER_NOT_FOUND_EXCEPTION", "Receiver not found");
        }
        //ResponseEntity<Boolean> response=restTemplate.postForEntity("http://TRANSACTION/transaction/"+userId,request,Boolean.class);
        ResponseEntity<Boolean> response=client.transaction(userId,request);

        return response.getBody();
    }
}
