package com.example.orderservice.messagequeue;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(topicPartitions = @TopicPartition(topic = "example-catalog-topic", partitions = { "0", "1","2" }))
    public void updateQty(ConsumerRecord<String, String> record) {
        log.info("MYSELF Kafka Message get  -> " + record.partition(),record.value());

    }
}
