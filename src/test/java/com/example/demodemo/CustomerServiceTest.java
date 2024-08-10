package com.example.demodemo;

import com.example.demodemo.Entities.Customer;
import com.example.demodemo.Repositories.CustomerRepository;
import com.example.demodemo.Services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCustomers() {
        // Given
        Customer customer = new Customer();
        customer.setId(1);
        customer.setCname("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setInitialDeposit(BigDecimal.valueOf(1000));
        customer.setNumberOfYears(5);
        customer.setSavingsType("Savings Regular");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        // When
        var customers = customerService.getAllCustomers();

        // Then
        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertEquals("John Doe", customers.get(0).getCname());
    }

    @Test
    public void testGetCustomerById() {
        // Given
        Customer customer = new Customer();
        customer.setId(1);
        customer.setCname("John Doe");
        customer.setEmail("john.doe@example.com");
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));

        // When
        var result = customerService.getCustomerById(1);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getCname());
    }

    @Test
    public void testAddCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setId(1);
        customer.setCname("John Doe");
        customer.setEmail("john.doe@example.com");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        var result = customerService.addCustomer(customer);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getCname());
        verify(customerRepository).save(customer);
    }

    @Test
    public void testUpdateCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setId(1);
        customer.setCname("John Doe Updated");
        customer.setEmail("john.doe.updated@example.com");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        var result = customerService.updateCustomer(customer);

        // Then
        assertNotNull(result);
        assertEquals("John Doe Updated", result.getCname());
        verify(customerRepository).save(customer);
    }

    @Test
    public void testDeleteCustomer() {
        // Given
        Integer customerId = 1;

        // When
        customerService.deleteCustomer(customerId);

        // Then
        verify(customerRepository).deleteById(customerId);
    }


}
