package com.yashwanth.kafka;

import lombok.*;
import org.apache.avro.reflect.AvroSchema;


@AvroSchema("{"
    + "\"type\":\"record\","
    + "\"name\":\"MessageDTO\","
    + "\"namespace\":\"com.yashwanth.kafka\","
    + "\"fields\":["
    + " {\"name\":\"messageId\", \"type\":\"string\"},"
    + " {\"name\":\"message\", \"type\":\"string\"},"
    + " {\"name\":\"mainTopicError\", \"type\":\"string\", \"default\":\"\"},"
    + " {\"name\":\"retry0Error\", \"type\":\"string\", \"default\":\"\"},"
    + " {\"name\":\"retry1Error\", \"type\":\"string\", \"default\":\"\"},"
    + " {\"name\":\"retry2Error\", \"type\":\"string\", \"default\":\"\"}"
    + " ]"
    + "}")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MessageDTO  {
    String messageId;
    String message;
    String retry0Error;
    String retry1Error;
    String retry2Error;
}
