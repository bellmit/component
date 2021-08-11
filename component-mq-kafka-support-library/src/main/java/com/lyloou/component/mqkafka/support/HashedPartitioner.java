package com.lyloou.component.mqkafka.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 依据key把消息发送到指定partition
 *
 * @author lilou
 */
@Slf4j
public class HashedPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);

        int numPartitions = partitions.size();
        int partition = Math.abs(Objects.hash(key) % numPartitions);
        log.info("Kafka 共有{}个分区，Topic-{}下的Key为{}的消息被分配到{}分区", numPartitions, topic, key, partition);
        return partition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
