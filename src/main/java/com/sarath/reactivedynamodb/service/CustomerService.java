package com.sarath.reactivedynamodb.service;

import com.sarath.reactivedynamodb.domain.Address;
import com.sarath.reactivedynamodb.domain.Customer;
import com.sarath.reactivedynamodb.repos.CustomerRepository;
import com.sarath.reactivedynamodb.util.Result;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.function.LongSupplier;

import static com.sarath.reactivedynamodb.util.Result.FAIL;
import static com.sarath.reactivedynamodb.util.Result.SUCCESS;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final LongSupplier getEpochSecond = () -> Instant.now().getEpochSecond();

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Result> createNewCustomer(Customer customer) {
        customer.setCreatedTimeStamp(getEpochSecond.getAsLong());
        CompletableFuture<Result> handle = customerRepository.save(customer)
                .handle((__, ex) -> ex == null ? SUCCESS : FAIL);

        return Mono.fromFuture(handle);
    }

    public Mono<Customer> getCustomerByCustomerId(String customerId) {
        CompletableFuture<Customer> customer = customerRepository.getCustomerByID(customerId)
                .whenComplete((cus, ex) -> {
                    if (null == cus) {
                        throw new IllegalArgumentException("Invalid customerId");
                    }
                })
                .exceptionally(ex -> new Customer());

        return Mono.fromFuture(customer);
    }

    public Mono<Address> queryAddressByCustomerId(String customerId) {
        SdkPublisher<Address> customerAddress = customerRepository.getCustomerAddress(customerId)
                .items()
                .map(Customer::getAddress);

        return Mono.from(customerAddress)
                .onErrorReturn(new Address());
    }

    public Mono<Result> updateExistingCustomer(Customer customer) {
        customer.setCreatedTimeStamp(getEpochSecond.getAsLong());
        CompletableFuture<Result> handle = customerRepository.getCustomerByID(customer.getCustomerID())
                .thenApply(retrievedCustomer -> {
                    if (null == retrievedCustomer) {
                        throw new IllegalArgumentException("Invalid CustomerID");
                    }
                    return retrievedCustomer;
                }).thenCompose(__ -> customerRepository.updateCustomer(customer))
                .handle((__, ex) -> ex == null ? SUCCESS : FAIL);

        return Mono.fromFuture(handle);
    }

    public Mono<Result> updateExistingOrCreateCustomer(Customer customer) {
        customer.setCreatedTimeStamp(getEpochSecond.getAsLong());
        CompletableFuture<Result> handle = customerRepository.updateCustomer(customer)
                .handle((__, ex) -> ex == null ? SUCCESS : FAIL);

        return Mono.fromFuture(handle);
    }

    public Mono<Result> deleteCustomerByCustomerId(String customerId) {
        CompletableFuture<Result> handle = customerRepository.deleteCustomerById(customerId)
                .handle((__, ex) -> ex == null ? SUCCESS : FAIL);

        return Mono.fromFuture(handle);
    }

    public Flux<Customer> getCustomerList() {
        return Flux.from(customerRepository.getAllCustomer().items())
                .onErrorReturn(new Customer());
    }

}
