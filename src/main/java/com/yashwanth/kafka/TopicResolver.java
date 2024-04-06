package com.yashwanth.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.retrytopic.DestinationTopic;
import org.springframework.kafka.retrytopic.RetryTopicNamesProviderFactory;


@Configuration
@Primary
public class TopicResolver implements RetryTopicNamesProviderFactory {

    @Value("${spring.kafka.topic-prefix}")
    String topicPrefix;

    @Value("${spring.profiles.active}")
    String env;
    DestinationTopic.Properties properties;

    @Override
    public RetryTopicNamesProvider createRetryTopicNamesProvider(DestinationTopic.Properties properties) {
        this.properties = properties;
        return new SuffixingRetryTopicNamesProvider(properties);
    }

    public class SuffixingRetryTopicNamesProvider implements RetryTopicNamesProvider {

        private final RetrySuffixer suffixer;
        public SuffixingRetryTopicNamesProvider(DestinationTopic.Properties properties) {
            this.suffixer = new RetrySuffixer(topicPrefix,properties.suffix(),env);
        }

        @Override
        public String getEndpointId(MethodKafkaListenerEndpoint<?, ?> endpoint) {
            return this.suffixer.maybeAddTo(endpoint.getId());
        }

        @Override
        public String getGroupId(MethodKafkaListenerEndpoint<?, ?> endpoint) {
            return this.suffixer.maybeAddTo(endpoint.getGroupId());
        }

        @Override
        public String getClientIdPrefix(MethodKafkaListenerEndpoint<?, ?> endpoint) {
            return this.suffixer.maybeAddTo(endpoint.getClientIdPrefix());
        }

        @Override
        public String getGroup(MethodKafkaListenerEndpoint<?, ?> endpoint) {
            return this.suffixer.maybeAddTo(endpoint.getGroup());
        }

        @Override
        public String getTopicName(String topic) {
            return this.suffixer.maybeAddTo(topic);
        }

    }

}


