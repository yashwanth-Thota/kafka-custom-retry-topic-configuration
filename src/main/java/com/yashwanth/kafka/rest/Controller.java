package com.yashwanth.kafka.rest;

import com.yashwanth.kafka.MessageDTO;
import com.yashwanth.kafka.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class Controller {


    final Producer producer;


    @PostMapping(value = "/sendNotification",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity sendNotification(@RequestBody MessageDTO messageDTO) {
        producer.send(messageDTO);
        return ResponseEntity.ok("Sent");
    }
}
