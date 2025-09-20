package com.fitness.userservice.services;

import com.fitness.userservice.dto.UserRequest;
import com.fitness.userservice.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRequest request);

    UserResponse getSingleUser(String userId);

    Boolean existsByUserId(String userId);
}
