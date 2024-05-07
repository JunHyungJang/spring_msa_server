package com.example.orderservice;

import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.OrderServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.persistence.criteria.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) throws IOException, InterruptedException {

		ConfigurableApplicationContext context = SpringApplication.run(OrderServiceApplication.class, args);
		startGrpcServer(context);
	}

//	private static void startGrpcServer(ConfigurableApplicationContext context, OrderService orderService) throws IOException, InterruptedException {
//		// 여기서 gRPC 서버를 가져오거나 생성하는 방법은 프로젝트의 구성에 따라 달라질 수 있습니다.
//		// 예를 들어, @Bean으로 gRPC 서버를 구성한 경우에는 해당 빈을 가져와서 사용하면 됩니다.
//		// 혹은 직접 생성하여 설정할 수도 있습니다.
//		// 아래 코드는 예시일 뿐이며, 실제로는 프로젝트에 맞게 적절히 수정해야 합니다.
//		Server server = ServerBuilder.forPort(8912)
//				.addService(new OrderServiceGrpc(orderService))
//				.build();
//		server.start();
//
//	}
@Bean
//@ConditionalOnBean(name = , value = )
public Server grpcServer(OrderService orderService) throws IOException {
	return ServerBuilder.forPort(8912)
			.addService(new OrderServiceGrpc(orderService))
			.build();
}

	private static void startGrpcServer(ConfigurableApplicationContext context) throws IOException, InterruptedException {
		Server grpcServer = context.getBean(Server.class);
		grpcServer.start();
	}
}
