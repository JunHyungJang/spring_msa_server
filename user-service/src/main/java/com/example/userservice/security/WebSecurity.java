package com.example.userservice.security;


import com.example.userservice.service.UserService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // 이 부분을 추가
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.nio.file.Path;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity

public class WebSecurity {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;
    private final AuthenticationConfiguration authenticationConfiguration;
    private static final String[] WHITE_LIST = {
            "/users/**",
            "/",
            "/**"
    };
    public WebSecurity(UserService userService, Environment env,AuthenticationConfiguration authenticationConfiguration, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(antMatcher("/actuator/**")).permitAll()
                            .requestMatchers(antMatcher("/**")).permitAll()
                            .requestMatchers(PathRequest.toH2Console()).permitAll()
                            .requestMatchers(PathRequest.toH2Console()).permitAll()
                            .requestMatchers(new IpAddressMatcher("127.0.0.1")).permitAll()

                            .requestMatchers(WHITE_LIST).permitAll()
                            .anyRequest().authenticated();
                })
                .addFilter(getAuthenticationFilter(authenticationConfiguration))
                .headers(header-> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
//        http.authorizeHttpRequests(
//                        request -> request.
//                                requestMatchers(PathRequest.toH2Console()).permitAll()
//
//                                .anyRequest().authenticated())
//                .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
//                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(authenticationConfiguration),userService, env,bCryptPasswordEncoder);
        return authenticationFilter;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }


}