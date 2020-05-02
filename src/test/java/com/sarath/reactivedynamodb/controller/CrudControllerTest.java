package com.sarath.reactivedynamodb.controller;

import com.sarath.reactivedynamodb.domain.Address;
import com.sarath.reactivedynamodb.domain.Customer;
import com.sarath.reactivedynamodb.service.CustomerService;
import com.sarath.reactivedynamodb.util.Result;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.sarath.reactivedynamodb.util.Result.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest
public class CrudControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private CustomerService customerService;

    @Test
    public void saveCustomer() {
        when(customerService.createNewCustomer(any())).thenReturn(Mono.just(SUCCESS));
        webTestClient.post()
                .uri("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(new Customer())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Result.class)
                .consumeWith(re -> assertThat(SUCCESS).isEqualByComparingTo(re.getResponseBody()));
    }

    @Test
    public void getCustomer() {
        when(customerService.getCustomerByCustomerId(any())).thenReturn(Mono.just(new Customer()));
        webTestClient.get()
                .uri("/get/111")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Customer.class)
                .consumeWith(Assert::assertNotNull);
    }

    @Test
    public void updateOrCreateCustomer() {
        when(customerService.updateExistingOrCreateCustomer(any())).thenReturn(Mono.just(SUCCESS));
        webTestClient.put()
                .uri("/updateCustomerOrCreate")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(new Customer())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Result.class)
                .consumeWith(re -> assertThat(SUCCESS).isEqualByComparingTo(re.getResponseBody()));
    }

    @Test
    public void deleteCustomer() {
        when(customerService.deleteCustomerByCustomerId(any())).thenReturn(Mono.just(SUCCESS));
        webTestClient.delete()
                .uri("/delete/111")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Result.class)
                .consumeWith(re -> assertThat(SUCCESS).isEqualByComparingTo(re.getResponseBody()));
    }

    @Test
    public void updateCustomer() {
        when(customerService.updateExistingCustomer(any())).thenReturn(Mono.just(SUCCESS));
        webTestClient.put()
                .uri("/updateCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(new Customer())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Result.class)
                .consumeWith(re -> assertThat(SUCCESS).isEqualByComparingTo(re.getResponseBody()));
    }

    @Test
    public void queryCustomerAddress() {
        when(customerService.queryAddressByCustomerId(any())).thenReturn(Mono.just(new Address()));
        webTestClient.get()
                .uri("/query/111")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Address.class)
                .consumeWith(Assert::assertNotNull);
    }
}