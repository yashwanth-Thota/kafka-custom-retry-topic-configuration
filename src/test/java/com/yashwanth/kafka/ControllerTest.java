package com.yashwanth.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yashwanth.kafka.config.TestConfiguration;
import com.yashwanth.kafka.rest.Controller;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest({Controller.class,Producer.class})
@AutoConfigureMockMvc
@ComponentScan(basePackageClasses = {Controller.class, TestConfiguration.class})
public class ControllerTest {

    @MockBean
    Producer producer;

    @Autowired
    MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void test_get(){
        var mapper=new ObjectMapper();
        var req=post("/sendNotification")
            .content(mapper.writeValueAsString(MessageDTO.builder().build()))
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        var resp=this.mockMvc.perform(req)
            .andExpect(status().is(200)).andReturn().getResponse();
        assertEquals("Sent",resp.getContentAsString());
    }
}
