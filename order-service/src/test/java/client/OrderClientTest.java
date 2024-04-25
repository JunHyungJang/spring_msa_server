package client;


import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.OrderServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class OrderClientTest {

    private com.jun.models.OrderServiceGrpcGrpc.OrderServiceGrpcBlockingStub blockingStub;
    private com.jun.models.OrderServiceGrpcGrpc.OrderServiceGrpcStub OrderServiceStub;
    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",8912)
                .usePlaintext()
                .build();
        this.blockingStub = com.jun.models.OrderServiceGrpcGrpc.newBlockingStub(managedChannel);
        this.OrderServiceStub = com.jun.models.OrderServiceGrpcGrpc.newStub(managedChannel);
    }

    @Test
    public void test(){
        com.jun.models.OrderRequest orderRequest = com.jun.models.OrderRequest.newBuilder().setUserId("hi").build();
//        this.blockingStub.getOrdersById(orderRequest)
        com.jun.models.OrderResponse orderResponse = this.blockingStub.getOrdersById(orderRequest);
//        log.info(orderResponse.getOrderObjects(0));
        com.jun.models.OrderObject firstOrderObject = orderResponse.getOrderObjectsList().get(0);

        // 가져온 값 로깅 또는 출력
        log.info("First Order Object - ID: {}, Qty: {}, Unit Price: {}, Total Price: {}, UserID: {}, OrderID: {}",
                firstOrderObject.getId(),
                firstOrderObject.getQty(),
                firstOrderObject.getUnitPrice(),
                firstOrderObject.getTotalPrice(),
                firstOrderObject.getUserId(),
                firstOrderObject.getOrderId());
    }

}
