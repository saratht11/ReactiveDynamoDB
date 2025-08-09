package com.sarath.reactivedynamodb.service;

import com.sarath.reactivedynamodb.domain.Address;
import com.sarath.reactivedynamodb.domain.Customer;
import com.sarath.reactivedynamodb.exception.CustomerNotFoundException;
import com.sarath.reactivedynamodb.repos.CustomerRepository;
import com.sarath.reactivedynamodb.util.Result;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
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
        Customer timestampedCustomer = setCurrentTimestamp(customer);
        return Mono.fromFuture(customerRepository.save(timestampedCustomer))
                   .thenReturn(SUCCESS)
                   .onErrorReturn(FAIL);
    }

    public Mono<Customer> getCustomerByCustomerId(String customerId) {
        return Mono.fromFuture(customerRepository.getCustomerByID(customerId))
                   .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with ID: " + customerId)));
    }

    public Mono<Address> queryAddressByCustomerId(String customerId) {
        return Mono.from(customerRepository.getCustomerAddress(customerId))
                   .map(Customer::getAddress)
                   .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with ID: " + customerId)));
    }

    public Mono<Result> updateExistingCustomer(Customer customer) {
        Customer timestampedCustomer = setCurrentTimestamp(customer);
        return Mono.fromFuture(customerRepository.getCustomerByID(customer.getCustomerID()))
                   .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with ID: " + customer.getCustomerID())))
                   .flatMap(existingCustomer -> Mono.fromFuture(customerRepository.updateCustomer(timestampedCustomer)))
                   .thenReturn(SUCCESS)
                   .onErrorReturn(FAIL);
    }

    public Mono<Result> updateExistingOrCreateCustomer(Customer customer) {
        Customer timestampedCustomer = setCurrentTimestamp(customer);
        return Mono.fromFuture(customerRepository.updateCustomer(timestampedCustomer))
                   .thenReturn(SUCCESS)
                   .onErrorReturn(FAIL);
    }

    public Mono<Result> deleteCustomerByCustomerId(String customerId) {
        return Mono.fromFuture(customerRepository.deleteCustomerById(customerId))
                   .thenReturn(SUCCESS)
                   .onErrorReturn(FAIL);
    }

    public Flux<Customer> getCustomerList() {
        return Flux.from(customerRepository.getAllCustomer()
                                           .items())
                   .onErrorResume(throwable -> {
                       // Log error and return empty flux instead of a single empty customer
                       return Flux.empty();
                   });
    }

    private Customer setCurrentTimestamp(Customer customer) {
        Customer timestampedCustomer = new Customer();
        timestampedCustomer.setCustomerID(customer.getCustomerID());
        timestampedCustomer.setfName(customer.getfName());
        timestampedCustomer.setlName(customer.getlName());
        timestampedCustomer.setContactNo(customer.getContactNo());
        timestampedCustomer.setAddress(customer.getAddress());
        timestampedCustomer.setCreatedTimeStamp(getEpochSecond.getAsLong());
        return timestampedCustomer;
    }

}
