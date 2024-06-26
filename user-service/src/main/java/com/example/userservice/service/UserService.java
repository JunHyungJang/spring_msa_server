package com.example.userservice.service;

import com.example.userservice.Dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.security.core.userdetails.UserDetailsService;


//@Component
public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserbyId(String userId);
    UserDto getUserbyId2(String userId);

    Iterable<UserEntity> getUserbyAll();

    String user_feign_test_impl();

    UserDto getUserDetailsByEmail(String userName);
}
