package com.example.userservice.controlller;


import com.example.userservice.Dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.example.userservice.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
public class UserController {

    private Environment env;
    private UserService userService;

    @Autowired
    private Greeting greeting;

    @Autowired
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }


    @GetMapping("/health_check")
    @Timed(value="users.status",longTask = true)
    public String status(){

        return String.format("It's working in user service on port"
        + ", port(local.server.port)=" + env.getProperty("local.server.port")
        + ", port(server.port)=" + env.getProperty("server.port")
        + ", token secret=" + env.getProperty("token.secret")
        + ", token expiration type =" + env.getProperty("token.expiration_time")

        );
    }
    @GetMapping("/welcome")
    @Timed(value="users.welcome",longTask = true)

    public String welcome(){
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userList = userService.getUserbyAll();
//        log.info("helloword");

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v,ResponseUser.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUsers(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserbyId(userId);
        log.info(userDto.getUserId());
        ResponseUser returnValue = new ModelMapper().map(userDto,ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
