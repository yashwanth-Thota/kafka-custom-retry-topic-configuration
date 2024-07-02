package com.yashwanth.kafka.config;

import com.yashwanth.kafka.MessageDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;

@org.springframework.context.annotation.Configuration
public class TestConfiguration {

    @Bean
    public KafkaTemplate<String, MessageDTO> kafkaTemplate(){
        return mock(KafkaTemplate.class);
    }
}
