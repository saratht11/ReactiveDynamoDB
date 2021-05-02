package com.sarath.reactivedynamodb.repos;

import com.sarath.reactivedynamodb.domain.Customer;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.concurrent.CompletableFuture;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.keyEqualTo;

@Service
public class CustomerRepository {

    private final DynamoDbAsyncTable<Customer> customerDynamoDbAsyncTable;

    public CustomerRepository(DynamoDbAsyncTable<Customer> customerDynamoDbAsyncTable) {
        this.customerDynamoDbAsyncTable = customerDynamoDbAsyncTable;
    }

    //CREATE
    public CompletableFuture<Void> save(Customer customer) {
        return customerDynamoDbAsyncTable.putItem(customer);
    }

    //READ
    public CompletableFuture<Customer> getCustomerByID(String customerId) {
        return customerDynamoDbAsyncTable.getItem(getKeyBuild(customerId));
    }

    //UPDATE
    public CompletableFuture<Customer> updateCustomer(Customer customer) {
        return customerDynamoDbAsyncTable.updateItem(customer);
    }

    //DELETE
    public CompletableFuture<Customer> deleteCustomerById(String customerId) {
        return customerDynamoDbAsyncTable.deleteItem(getKeyBuild(customerId));
    }

    //READ_CUSTOMER_ADDRESS_ONLY
    public SdkPublisher<Customer> getCustomerAddress(String customerId) {
        return customerDynamoDbAsyncTable.query(r -> r.queryConditional(keyEqualTo(k -> k.partitionValue(customerId)))
                                                      .addAttributeToProject("CustomerAddress"))
                                         .items();
    }

    //GET_ALL_ITEM
    public PagePublisher<Customer> getAllCustomer() {
        return customerDynamoDbAsyncTable.scan();
    }

    private Key getKeyBuild(String customerId) {
        return Key.builder()
                  .partitionValue(customerId)
                  .build();
    }

}

