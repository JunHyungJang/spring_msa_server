package com.example.userservice.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
//import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Data
public class    RequestUser {

//    @NotNull(message = "Email can not be null");
//    @Size(min = 2, message= "Email not be less than two characters");
    @NotNull(message = "hello")
    @Email
    private String email;

    private String name;

    private String pwd;

}
