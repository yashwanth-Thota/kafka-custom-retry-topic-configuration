package com.yashwanth.kafka;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CustomProducerInterceptor implements ProducerInterceptor<String,MessageDTOV1> {

    @Override
    public ProducerRecord<String, MessageDTOV1> onSend(ProducerRecord<String, MessageDTOV1> record) {
        if(record.headers().lastHeader("MAIN_ERROR")!=null){
            record.value().setMainTopicError(new String(record.headers().lastHeader("MAIN_ERROR").value()));
        }
        if(record.headers().lastHeader("RETRY_0_ERROR")!=null){
            record.value().setRetry0Error(new String(record.headers().lastHeader("RETRY_0_ERROR").value()));
        }
        if(record.headers().lastHeader("RETRY_1_ERROR")!=null){
            record.value().setRetry1Error(new String(record.headers().lastHeader("RETRY_1_ERROR").value()));
        }
        if(record.headers().lastHeader("RETRY_2_ERROR")!=null){
            record.value().setRetry2Error(new String(record.headers().lastHeader("RETRY_2_ERROR").value()));
        }
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
