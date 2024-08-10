package com.example.demodemo.Services;

import com.example.demodemo.Entities.Customer;
import com.example.demodemo.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Retrieve a customer by ID
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    // Add a new customer
    public Customer addCustomer(Customer customer) {
        if (customerExists(customer.getId())) {
            throw new IllegalArgumentException("Customer with ID " + customer.getId() + " already exists.");
        }
        return customerRepository.save(customer);
    }

    // Update an existing customer
    public Customer updateCustomer(Customer customer) {
        if (!customerExists(customer.getId())) {
            throw new IllegalArgumentException("Customer with ID " + customer.getId() + " does not exist.");
        }
        return customerRepository.save(customer);
    }

    // Delete a customer by ID
    public void deleteCustomer(Integer id) {
        if (!customerExists(id)) {
            throw new IllegalArgumentException("Customer with ID " + id + " does not exist.");
        }
        customerRepository.deleteById(id);
    }

    // Check if a customer exists by ID
    public boolean customerExists(Integer id) {
        return customerRepository.existsById(id);
    }

    // Class to represent each year's projection in the compound interest table
    public static class InvestmentProjection {
        private int year;
        private BigDecimal startingAmount;
        private BigDecimal interest;
        private BigDecimal endingBalance;

        public InvestmentProjection(int year, BigDecimal startingAmount, BigDecimal interest, BigDecimal endingBalance) {
            this.year = year;
            this.startingAmount = startingAmount;
            this.interest = interest;
            this.endingBalance = endingBalance;
        }

        // Getters and setters
        public int getYear() {
            return year;
        }

        public BigDecimal getStartingAmount() {
            return startingAmount;
        }

        public BigDecimal getInterest() {
            return interest;
        }

        public BigDecimal getEndingBalance() {
            return endingBalance;
        }
    }

    // Calculate compound interest for a given customer and return a list of projections
    public List<InvestmentProjection> calculateCompoundInterest(BigDecimal principal, double rate, int years) {
        List<InvestmentProjection> projection = new ArrayList<>();
        BigDecimal currentAmount = principal;

        for (int i = 1; i <= years; i++) {
            BigDecimal interest = currentAmount.multiply(BigDecimal.valueOf(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal endingBalance = currentAmount.add(interest);
            projection.add(new InvestmentProjection(i, currentAmount, interest, endingBalance));
            currentAmount = endingBalance; // Update the current amount for the next year
        }

        return projection;
    }
}
