package com.sarath.reactivedynamodb.service;

import com.sarath.reactivedynamodb.domain.Address;
import com.sarath.reactivedynamodb.domain.Customer;
import com.sarath.reactivedynamodb.repos.CustomerRepository;
import com.sarath.reactivedynamodb.util.Result;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;
import java.util.function.LongSupplier;

import static com.sarath.reactivedynamodb.util.Result.FAIL;
import static com.sarath.reactivedynamodb.util.Result.SUCCESS;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final LongSupplier getEpochSecond = () -> Instant.now()
                                                             .getEpochSecond();

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Result> createNewCustomer(Customer customer) {
        customer.setCreatedTimeStamp(getEpochSecond.getAsLong());
        return Mono.fromFuture(customerRepository.save(customer))
                   .thenReturn(SUCCESS)
                   .onErrorReturn(FAIL);
    }

    public Mono<Customer> getCustomerByCustomerId(String customerId) {
        return Mono.fromFuture(customerRepository.getCustomerByID(customerId))
                   .doOnSuccess(Objects::requireNonNull)
                   .onErrorReturn(new Customer());
    }

    public Mono<Address> queryAddressByCustomerId(String customerId) {
        return Mono.from(customerRepository.getCustomerAddress(customerId))
                   .map(Customer::getAddress)
                   .doOnSuccess(Objects::requireNonNull)
                   .onErrorReturn(new Address());
    }

    public Mono<Result> updateExistingCustomer(Customer customer) {
        customer.setCreatedTimeStamp(getEpochSecond.getAsLong());
        return Mono.fromFuture(customerRepository.getCustomerByID(customer.getCustomerID()))
                   .doOnSuccess(Objects::requireNonNull)
                   .doOnNext(__ -> customerRepository.updateCustomer(customer))
                   .doOnSuccess(Objects::requireNonNull)
                   .thenReturn(SUCCESS)
                   .onErrorReturn(FAIL);
    }

    public Mono<Result> updateExistingOrCreateCustomer(Customer customer) {
        customer.setCreatedTimeStamp(getEpochSecond.getAsLong());
        return Mono.fromFuture(customerRepository.updateCustomer(customer))
                   .thenReturn(SUCCESS)
                   .onErrorReturn(FAIL);
    }

    public Mono<Result> deleteCustomerByCustomerId(String customerId) {
        return Mono.fromFuture(customerRepository.deleteCustomerById(customerId))
                   .doOnSuccess(Objects::requireNonNull)
                   .thenReturn(SUCCESS)
                   .onErrorReturn(FAIL);
    }

    public Flux<Customer> getCustomerList() {
        return Flux.from(customerRepository.getAllCustomer()
                                           .items())
                   .onErrorReturn(new Customer());
    }

}
