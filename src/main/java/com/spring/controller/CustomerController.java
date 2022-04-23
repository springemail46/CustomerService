package com.spring.controller;

import com.spring.classes.MyOrders;
import com.spring.entity.Customer;
import com.spring.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @PostMapping("/")
    public Customer saveCustomer(@RequestBody Customer customer){
        return service.saveCustomer(customer);
    }
    @GetMapping("/{cid}")
    public MyOrders getCustomerDetails(@PathVariable int cid){
        return service.getCustomer(cid);
    }
    @GetMapping("/")
    public List<Customer> getAllCustomer(){
        return service.getAllCustomer();
    }
    @PutMapping("/{cid}")
    public Customer updateCustomer(@RequestBody Customer customer, @PathVariable int cid){
        return service.updateCustomer(customer, cid);
    }
    @DeleteMapping("/{cid}")
    public Map<String, Boolean> deleteCustomer(@PathVariable int cid){
        return service.deleteCustomer(cid);
    }
}
