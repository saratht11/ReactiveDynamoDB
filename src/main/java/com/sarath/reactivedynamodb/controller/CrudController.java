package com.sarath.reactivedynamodb.controller;

import com.sarath.reactivedynamodb.domain.Address;
import com.sarath.reactivedynamodb.domain.Customer;
import com.sarath.reactivedynamodb.service.CustomerService;
import com.sarath.reactivedynamodb.util.Result;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CrudController {

    private final CustomerService customerService;

    public CrudController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/save")//C
    public Mono<Result> saveCustomer(@RequestBody Customer customer) {
        return customerService.createNewCustomer(customer);
    }

    @GetMapping("/get/{customerId}")//R
    public Mono<Customer> getCustomer(@PathVariable() String customerId) {
        return customerService.getCustomerByCustomerId(customerId);
    }

    @PutMapping("/updateCustomerOrCreate")//U
    public Mono<Result> updateOrCreateCustomer(@RequestBody Customer customer) {
        return customerService.updateExistingOrCreateCustomer(customer);
    }

    @DeleteMapping("/delete/{customerId}")//D
    public Mono<Result> deleteCustomer(@PathVariable() String customerId) {
        return customerService.deleteCustomerByCustomerId(customerId);
    }

    @PutMapping("/updateCustomer")
    public Mono<Result> updateCustomer(@RequestBody Customer customer) {
        return customerService.updateExistingCustomer(customer);
    }

    @GetMapping("/query/{customerId}")
    public Mono<Address> queryCustomerAddress(@PathVariable() String customerId) {
        return customerService.queryAddressByCustomerId(customerId);
    }

    @GetMapping("/allCustomerList")
    public Flux<Customer> getAllCustomer() {
        return customerService.getCustomerList();
    }

    //batchGetItem

    //batchWriteItem


}
