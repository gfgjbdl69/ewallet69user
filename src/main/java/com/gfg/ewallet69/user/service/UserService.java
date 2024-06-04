package com.gfg.ewallet69.user.service;

import com.gfg.ewallet69.user.domain.User;
import com.gfg.ewallet69.user.service.resource.TransactionRequest;
import com.gfg.ewallet69.user.service.resource.UserRequest;
import com.gfg.ewallet69.user.service.resource.UserResponse;

public interface UserService {

    public void createUser(User user) ;
    public UserResponse getUser(String id);

    public User deleteUser(String id);

    public User updateUser(UserRequest userRequest, String id);

    public boolean transfer(Long userId, TransactionRequest request);
}
