package com.example.userservice.service;


import com.example.userservice.Dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
//import org.modelmapper.
//import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment env;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Environment env) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedpwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
//        log.debug(returnUserDto);
        //다시확인하기 {}값으로나옴
        return returnUserDto;
    }

    @Override
    public UserDto getUserbyId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            log.info("NO user name");
        }
        UserDto userDto = new ModelMapper().map(userEntity,UserDto.class);
        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserbyAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
//        log.info(userEntity.getEmail());
        return new User(userEntity.getEmail(),userEntity.getEncryptedpwd(),
                true,true,true,true,new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDto userDto = new ModelMapper().map(userEntity,UserDto.class);
        return userDto;
    }
}
