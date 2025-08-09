package com.sarath.reactivedynamodb.config;

import com.sarath.reactivedynamodb.domain.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {
    private final String dynamoDbEndPointUrl;

    public DynamoDbConfig(@Value("${aws.dynamodb.endpoint}") String dynamoDbEndPointUrl) {
        this.dynamoDbEndPointUrl = dynamoDbEndPointUrl;
    }

    @Bean
    public DynamoDbAsyncClient getDynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
                                  .credentialsProvider(ProfileCredentialsProvider.create("default"))
                                  .endpointOverride(URI.create(dynamoDbEndPointUrl))
                                  .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient(DynamoDbAsyncClient dynamoDbAsyncClient) {
        return DynamoDbEnhancedAsyncClient.builder()
                                          .dynamoDbClient(dynamoDbAsyncClient)
                                          .build();
    }

    @Bean
    public DynamoDbAsyncTable<Customer> getDynamoDbAsyncCustomer(DynamoDbEnhancedAsyncClient asyncClient) {
        return asyncClient.table(Customer.class.getSimpleName(), TableSchema.fromBean(Customer.class));
    }

    @Bean("dynamoScheduler")
    public Scheduler dynamoScheduler() {
        return Schedulers.newElastic("dynamo-db");
    }

}
