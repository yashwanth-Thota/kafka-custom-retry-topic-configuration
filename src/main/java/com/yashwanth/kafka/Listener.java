package com.yashwanth.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.RetryTopicHeaders;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

//@Service
public class Listener {

    @RetryableTopic(
        attempts = "4",
        backoff = @Backoff(100)
    )
    @KafkaListener(
        topics = "${spring.kafka.template.default-topic}",
        clientIdPrefix = "${spring.kafka.consumer.client-id}"
    )
    public void handle(
        ConsumerRecord<String, MessageDTOV1> record,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS) Optional<Integer> attemptOptional
    ) {
        try {
            int attempt = attemptOptional.orElse(1);
            System.out.println(String.format("Topic : %s, Offset : %s , attempt : %s",topic, record.offset(), attempt));
            throw new RuntimeException("Failed to handle");
        }catch (Exception e) {
            applyException(record.value(), topic,e);
            throw e;
        }
    }

    private void applyException(MessageDTOV1 value, String topic,Exception exception) {
        if(topic.contains("retry-0")){
            value.setRetry0Error(exception.getMessage());
        }
        else if(topic.contains("retry-1")){
            value.setRetry1Error(exception.getMessage());
        }
        else if(topic.contains("retry-2")){
            value.setRetry2Error(exception.getMessage());
        }
        else{
            value.setMainTopicError(exception.getMessage());
        }
    }

    private RecordHeader getErrorHeader(String topic, Exception e){
        String headername;
        if(topic.contains("retry-0")){
            headername="RETRY_0_ERROR";
        }
        else if(topic.contains("retry-1")){
            headername="RETRY_1_ERROR";
        }
        else if(topic.contains("retry-2")){
            headername="RETRY_2_ERROR";
        }else {
            headername="MAIN_ERROR";
        }
        return new RecordHeader(headername,e.getMessage().getBytes(StandardCharsets.UTF_8));
    }

}
