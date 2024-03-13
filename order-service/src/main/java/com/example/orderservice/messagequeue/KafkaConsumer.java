package com.example.orderservice.messagequeue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(topics="example-catalog-topic")
    public void updateQty(String kafkaMessage) {
        log.info("MYSELF Kafka Message get  -> " + kafkaMessage);

    }
}
