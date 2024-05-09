package com.example.userservice;

import com.example.userservice.Dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.h2.engine.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserServiceApplicationTests {


	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();
//	@Mock
//	UserRepository userRepository;
////
//	@Mock
//	UserService userService;
//
//	@Captor
//	ArgumentCaptor<UserEntity> userCaptor;
	@Test
	@DisplayName("Health check")
	public void testStatus() throws Exception {
		mockMvc.perform(get("/health_check"))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName("welcome")
	public void testWelcome() throws Exception{
		mockMvc.perform(get("/welcome"))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName("craeteUser")
	public void createUser() throws Exception {

		RequestUser requestUser = RequestUser
				.builder()
				.email("kevin0459@naver.com")
				.name("jun")
				.pwd("12345678")
				.build();

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestUser)))
				.andExpect(status().isCreated());

	}
}
