package com.gfg.ewallet69.user.service.impl;

import com.gfg.ewallet69.user.domain.User;
import com.gfg.ewallet69.user.exception.UserException;
import com.gfg.ewallet69.user.repository.UserRepository;
import com.gfg.ewallet69.user.service.UserService;
import com.gfg.ewallet69.user.service.resource.UserRequest;
import com.gfg.ewallet69.user.service.resource.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    PasswordEncoder encoder;

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
        return null;
    }
}
