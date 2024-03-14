package com.example.orderservice.messagequeue;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {

        if (bytes == null)
            throw new InvalidRecordException("no message key");

        if (((String)key).equals("msgKey3"))
            return 2;

        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        Random random  = new Random();
        int randNumber = random.nextInt(numPartitions);
        return randNumber;

    }
    @Override
    public void close(){}

    @Override
    public void configure(Map<String, ?> map) {

    }
}
