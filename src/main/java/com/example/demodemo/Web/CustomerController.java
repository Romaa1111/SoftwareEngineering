package com.example.demodemo.Web;

import com.example.demodemo.Entities.Customer;
import com.example.demodemo.Services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class CustomerController {

    @Autowired
    private CustomerService customerService; // Use the service layer instead of repository

    @GetMapping(path = "/")
    public String initialPage(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("customer", new Customer());
        return "Main";
    }

    @PostMapping("/addOrUpdateCustomer")
    public String addOrUpdateCustomer(@ModelAttribute Customer customer, Model model) {
        if (customer.getId() > 0) {
            // Update existing customer
            customerService.updateCustomer(customer);
        } else {
            // Add new customer
            customerService.addCustomer(customer);
        }

        // Refresh the list of customers to show the latest data in the view
        model.addAttribute("customers", customerService.getAllCustomers());

        // Create a new Customer object for the form to be ready for new entries
        model.addAttribute("customer", new Customer());

        return "Main"; // Change this if needed to the name of your actual view
    }

    @GetMapping(path = "/edit")
    public String editCustomer(@RequestParam("id") int id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            model.addAttribute("customer", customer);
        } else {
            model.addAttribute("error", "Customer not found");
        }
        model.addAttribute("customers", customerService.getAllCustomers());
        return "Main";
    }

    @GetMapping("/delete")
    public String deleteCustomer(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        customerService.deleteCustomer(id);

        redirectAttributes.addFlashAttribute("message", "Customer deleted successfully.");

        return "redirect:/";
    }

    @GetMapping("/projectedInvestment")
    public String projectedInvestment(@RequestParam("id") int id, Model model) {
        Customer customer = customerService.getCustomerById(id);

        if (customer == null) {
            model.addAttribute("error", "Customer not found");
            return "Main";
        }

        double rate = "Savings De-luxe".equals(customer.getSavingsType()) ? 0.15 : 0.10;
        BigDecimal amount = customer.getInitialDeposit();
        int years = customer.getNumberOfYears();

        List<Projection> projections = new ArrayList<>();

        for (int i = 1; i <= years; i++) {
            BigDecimal interest = amount.multiply(BigDecimal.valueOf(rate)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal endingBalance = amount.add(interest).setScale(2, RoundingMode.HALF_UP);

            projections.add(new Projection(i, amount.setScale(2, RoundingMode.HALF_UP), interest, endingBalance));

            // The ending balance of this year becomes the starting amount of the next year
            amount = endingBalance;
        }

        model.addAttribute("projections", projections);
        model.addAttribute("customer", customer);

        return "InvestmentProjection"; // The name of your Thymeleaf template
    }

    // Inner class to hold projection data
    public static class Projection {
        private int year;
        private BigDecimal startingAmount;
        private BigDecimal interest;
        private BigDecimal endingBalance;

        public Projection(int year, BigDecimal startingAmount, BigDecimal interest, BigDecimal endingBalance) {
            this.year = year;
            this.startingAmount = startingAmount;
            this.interest = interest;
            this.endingBalance = endingBalance;
        }

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
}
