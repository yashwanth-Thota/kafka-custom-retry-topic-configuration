package com.yashwanth.kafka.config;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.RestService;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
public class SchemaRegistryConfig {

    @Autowired
    KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<?,?> producerFactory() throws Exception {

        var props=kafkaProperties.getProperties();

        final RestService restService = new RestService(props.get("schema.registry.url"));
        restService.setSslSocketFactory(getSslSocketFactory());

        final SchemaRegistryClient client = new CachedSchemaRegistryClient(restService, 200);

        final Serializer<Object> valueSerializer = new KafkaAvroSerializer(client);
        final Serializer<String> keySerializer = new StringSerializer();

        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties(), keySerializer, valueSerializer);
    }

    private SSLSocketFactory getSslSocketFactory() throws Exception {
        var props=kafkaProperties.getProperties();
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream trustStoreStream = new FileInputStream(props.get(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG))) {
            trustStore.load(trustStoreStream, props.get(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG).toCharArray());
        }

        // Load the keystore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream keyStoreStream = new FileInputStream(props.get(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG))) {
            keyStore.load(keyStoreStream, props.get(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG).toCharArray());
        }

        // Initialize TrustManager
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        // Initialize KeyManager
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, props.get(SslConfigs.SSL_KEY_PASSWORD_CONFIG).toCharArray());

        // Initialize SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return sslContext.getSocketFactory();
    }

}
