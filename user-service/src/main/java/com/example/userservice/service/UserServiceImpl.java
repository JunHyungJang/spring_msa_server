package com.example.userservice.service;


import com.example.userservice.Dto.UserDto;
import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
//import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
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

    RestTemplate restTemplate;
    OrderServiceClient orderServiceClient;
    private com.jun.models.OrderServiceGrpcGrpc.OrderServiceGrpcBlockingStub blockingStub;

    CircuitBreakerFactory circuitBreakerFactory;

    public UserServiceImpl(CircuitBreakerFactory circuitBreakerFactory, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Environment env, RestTemplate restTemplate, OrderServiceClient orderServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",8912)
                .usePlaintext()
                .build();
        this.blockingStub = com.jun.models.OrderServiceGrpcGrpc.newBlockingStub(managedChannel);
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
    public UserDto getUserbyId2(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            log.info("NO user name");
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        CircuitBreaker circuitBreaker = (CircuitBreaker) circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> orderList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());
        com.jun.models.OrderRequest orderRequest = com.jun.models.OrderRequest.newBuilder().setUserId("c8580b94-0df8-42f7-ab3f-562794bcecaa").build();
//        this.blockingStub.getOrdersById(orderRequest)
//        com.jun.models.OrderResponse orderResponse = this.blockingStub.getOrdersById(orderRequest);
        userDto.setOrders(orderList);
        return userDto;
    }

    @Override
    public UserDto getUserbyId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            log.info("NO user name");
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        CircuitBreaker circuitBreaker = (CircuitBreaker) circuitBreakerFactory.create("circuitbreaker");
        com.jun.models.OrderRequest orderRequest = com.jun.models.OrderRequest.newBuilder().setUserId(userId).build();
        com.jun.models.OrderResponse orderResponse = this.blockingStub.getOrdersById(orderRequest);
        List<ResponseOrder> orderList = new ArrayList<>();

        for (com.jun.models.OrderObject orderObject : orderResponse.getOrderObjectsList()) {
            ResponseOrder responseOrder = new ResponseOrder();
//            long id = orderObject.getId();
            responseOrder.setOrderId(String.valueOf(orderObject.getId()));
            responseOrder.setQty((int) orderObject.getQty());
            responseOrder.setUnitPrice(orderObject.getUnitPrice());
            responseOrder.setTotalPrice(orderObject.getTotalPrice());
            responseOrder.setProductId(orderObject.getProductId());
            orderList.add(responseOrder);
        }

        userDto.setOrders(orderList);
//        this.blockingStub.getOrdersById(orderRequest)
//        com.jun.models.OrderResponse orderResponse = this.blockingStub.getOrdersById(orderRequest);
//        userDto.setOrders(orderList);
        return userDto;
    }



//    @Override
//    public UserDto getUserById2(String userId) {
//
//
//
//    }


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
        return new User(userEntity.getEmail(), userEntity.getEncryptedpwd(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public String user_feign_test_impl() {
        CircuitBreaker circuitBreaker = (CircuitBreaker) circuitBreakerFactory.create("circuitbreaker");
        String answer = circuitBreaker.run(() -> orderServiceClient.test1());

        return answer;

    }
}
