package com.example.userservice.service;

import com.example.userservice.Dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;


//@Component
public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserbyId(String userId);
    Iterable<UserEntity> getUserbyAll();


    UserDto getUserDetailsByEmail(String userName);
}
