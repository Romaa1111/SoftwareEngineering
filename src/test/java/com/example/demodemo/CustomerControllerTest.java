package com.example.demodemo;

import com.example.demodemo.Entities.Customer;
import com.example.demodemo.Services.CustomerService;
import com.example.demodemo.Web.CustomerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testInitialPage() throws Exception {
        Customer customer1 = new Customer();
        customer1.setId(1);
        customer1.setCname("John Doe");
        customer1.setEmail("john@example.com");

        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setCname("Jane Doe");
        customer2.setEmail("jane@example.com");

        when(customerService.getAllCustomers()).thenReturn(List.of(customer1, customer2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("Main"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    public void testAddOrUpdateCustomer() throws Exception {
        mockMvc.perform(post("/addOrUpdateCustomer")
                        .param("id", "0") // Use 0 for a new customer
                        .param("cname", "John Doe")
                        .param("email", "john@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("message"));

        verify(customerService).addCustomer(any(Customer.class));
    }

    @Test
    public void testEditCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setCname("John Doe");
        customer.setEmail("john@example.com");

        when(customerService.getCustomerById(1)).thenReturn(customer);

        mockMvc.perform(get("/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("Main"))
                .andExpect(model().attribute("customer", customer))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1);

        mockMvc.perform(get("/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("message"));

        verify(customerService).deleteCustomer(1);
    }
}
