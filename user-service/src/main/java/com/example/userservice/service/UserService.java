package com.example.userservice.service;

import com.example.userservice.Dto.UserDto;
import com.example.userservice.jpa.UserEntity;


//@Component
public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserbyId(String userId);
    Iterable<UserEntity> getUserbyAll();

}
