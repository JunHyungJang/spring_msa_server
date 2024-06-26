package com.example.catalogservice.messagequeue;

import com.example.catalogservice.entity.CatalogEntity;
import com.example.catalogservice.entity.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KafkaConsumer {
    CatalogRepository repository;

    @Autowired
    public KafkaConsumer(CatalogRepository repository) {
        this.repository= repository;
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "example-catalog-topic", partitions = { "0", "1","2" }))
    public void updateQty(String kafkaMessage) {
        log.info("Kafka Message -> " + kafkaMessage);
        Map<Object,Object> map = new HashMap<>();
        CatalogEntity entity = repository.findByProductId((String)map.get("productId"));
        if (entity!=null) {
            entity.setStock(entity.getStock()-(Integer)map.get("qty"));
            repository.save(entity);
        }
    }

}
