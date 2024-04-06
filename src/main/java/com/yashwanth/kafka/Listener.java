package com.yashwanth.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Service;

@Service
public class Listener {

    @RetryableTopic(
        attempts = "4"
    )
    @KafkaListener(
        topics = "${spring.kafka.template.default-topic}",
        clientIdPrefix = "${spring.kafka.consumer.client-id}"
    )
    public void handle(ConsumerRecord<String,String> record){
        System.out.println(record.headers());
        throw new RuntimeException("Failed to handle");
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String,String> record){
        System.out.println(record.value());
    }

}
