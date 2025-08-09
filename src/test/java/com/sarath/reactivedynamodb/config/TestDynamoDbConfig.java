package com.sarath.reactivedynamodb.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestDynamoDbConfig {

    @Bean
    @Primary
    public DynamoDbAsyncClient testDynamoDbAsyncClient() {
        return mock(DynamoDbAsyncClient.class);
    }

    @Bean
    @Primary
    public DynamoDbEnhancedAsyncClient testDynamoDbEnhancedAsyncClient() {
        return mock(DynamoDbEnhancedAsyncClient.class);
    }

    @Bean
    @Primary
    public DynamoDbAsyncTable testDynamoDbAsyncTable() {
        return mock(DynamoDbAsyncTable.class);
    }
}