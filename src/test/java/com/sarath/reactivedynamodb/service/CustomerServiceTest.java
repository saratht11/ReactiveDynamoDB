package com.sarath.reactivedynamodb.service;

import com.sarath.reactivedynamodb.domain.Customer;
import com.sarath.reactivedynamodb.exception.CustomerNotFoundException;
import com.sarath.reactivedynamodb.repos.CustomerRepository;
import com.sarath.reactivedynamodb.util.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void getCustomerByCustomerId_WhenCustomerExists_ShouldReturnCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerID("123");
        when(customerRepository.getCustomerByID("123"))
                .thenReturn(CompletableFuture.completedFuture(customer));

        // When & Then
        StepVerifier.create(customerService.getCustomerByCustomerId("123"))
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    public void getCustomerByCustomerId_WhenCustomerNotFound_ShouldThrowException() {
        // Given
        when(customerRepository.getCustomerByID("123"))
                .thenReturn(CompletableFuture.completedFuture(null));

        // When & Then
        StepVerifier.create(customerService.getCustomerByCustomerId("123"))
                .expectError(CustomerNotFoundException.class)
                .verify();
    }

    @Test
    public void createNewCustomer_WhenSuccessful_ShouldReturnSuccess() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerID("123");
        when(customerRepository.save(any(Customer.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        // When & Then
        StepVerifier.create(customerService.createNewCustomer(customer))
                .expectNext(Result.SUCCESS)
                .verifyComplete();
    }

    @Test
    public void createNewCustomer_WhenFailed_ShouldReturnFail() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerID("123");
        CompletableFuture<Void> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Database error"));
        when(customerRepository.save(any(Customer.class)))
                .thenReturn(failedFuture);

        // When & Then
        StepVerifier.create(customerService.createNewCustomer(customer))
                .expectNext(Result.FAIL)
                .verifyComplete();
    }

    @Test
    public void updateExistingCustomer_WhenCustomerNotFound_ShouldReturnFail() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerID("123");
        when(customerRepository.getCustomerByID("123"))
                .thenReturn(CompletableFuture.completedFuture(null));

        // When & Then
        StepVerifier.create(customerService.updateExistingCustomer(customer))
                .expectNext(Result.FAIL)
                .verifyComplete();
    }

    @Test
    public void getCustomerList_WhenError_ShouldReturnEmptyFlux() {
        // Given
        PagePublisher<Customer> mockPagePublisher = mock(PagePublisher.class);
        when(customerRepository.getAllCustomer()).thenReturn(mockPagePublisher);
        // Create a mock SdkPublisher that throws an error
        software.amazon.awssdk.core.async.SdkPublisher<Customer> mockSdkPublisher = mock(software.amazon.awssdk.core.async.SdkPublisher.class);
        when(mockPagePublisher.items()).thenReturn(mockSdkPublisher);

        // When & Then
        StepVerifier.create(customerService.getCustomerList())
                .verifyComplete();
    }
}