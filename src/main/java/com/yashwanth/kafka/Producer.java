package com.yashwanth.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Producer {
    //@Autowired
    KafkaTemplate kafkaTemplate;

    public void send(MessageDTO messageDTO){
        try {
            kafkaTemplate.sendDefault(UUID.randomUUID().toString(),MessageDTOV1.newBuilder()
                .setMessageId(UUID.randomUUID().toString()).setMessage("Message sent "+ UUID.randomUUID()).build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
