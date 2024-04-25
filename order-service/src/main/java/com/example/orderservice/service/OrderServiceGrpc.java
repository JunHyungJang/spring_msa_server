package com.example.orderservice.service;

import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.jun.models.OrderRequest;
import com.jun.models.OrderResponse;
import io.grpc.stub.StreamObserver;
import com.jun.models.OrderObject;

import org.hibernate.query.Order;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceGrpc extends com.jun.models.OrderServiceGrpcGrpc.OrderServiceGrpcImplBase {

//    OrderService orderService;
//
//    public OrderServiceGrpc( OrderService orderService) {
//        this.orderService=orderService;
//    }
    @Override
    public void getOrdersById(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        String userId = request.getUserId();
//        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
        List<OrderEntity> orderList = new ArrayList<>();

// 임시 OrderEntity 생성
        OrderEntity order1 = new OrderEntity();
        order1.setId(1L);
        order1.setProductId("product1");
        order1.setQty(2);
        order1.setUnitPrice(100);
        order1.setTotalPrice(200);
        order1.setUserId("user1");
        order1.setOrderId("order1");

        orderList.add(order1);



        OrderResponse.Builder responseBuilder = OrderResponse.newBuilder();

        for (OrderEntity orderEntity: orderList) {
            OrderObject orderObject = OrderObject.newBuilder()
                    .setId(orderEntity.getId())
                    .setQty(orderEntity.getQty())
                    .setUnitPrice(orderEntity.getUnitPrice())
                    .setTotalPrice(orderEntity.getTotalPrice())
                    .setUserId(orderEntity.getUserId())
                    .setOrderId(orderEntity.getOrderId())
                    .build();
            responseBuilder.addOrderObjects(orderObject);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
