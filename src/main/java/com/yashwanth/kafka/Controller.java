package com.yashwanth.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class Controller {


    final KafkaTemplate<String,String> kafkaTemplate;
    @GetMapping("/sendNotification")
    public ResponseEntity sendNotification(){
        kafkaTemplate.sendDefault(UUID.randomUUID().toString(),"Hello!");
        return ResponseEntity.ok("Sent");
    }
}
