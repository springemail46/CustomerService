package com.spring.controller;

import com.spring.classes.MyOrders;
import com.spring.entity.Customer;
import com.spring.exception.CustomerControllerException;
import com.spring.exception.CustomerServiceException;
import com.spring.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @PostMapping("/")
    public ResponseEntity<?>  saveCustomer(@RequestBody Customer customer) {
        try {
            Customer saveCus = service.saveCustomer(customer);
            return new ResponseEntity<Customer>(saveCus, HttpStatus.CREATED);
        } catch (CustomerControllerException ex) {
            CustomerControllerException ce = new CustomerControllerException(ex.getErrorCode(), ex.getErrorMessage());
            return new ResponseEntity<CustomerControllerException>(ce, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            CustomerControllerException ce = new CustomerControllerException("708", "Something went wrong in Customer controller");
            return new ResponseEntity<CustomerControllerException>(ce, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{cid}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable int cid){
        try {
            MyOrders myOrders = service.getCustomer(cid);
            return new ResponseEntity<MyOrders>(myOrders, HttpStatus.OK);
        }catch (CustomerControllerException ex){
            CustomerControllerException cce = new CustomerControllerException(ex.getErrorCode(), ex.getErrorMessage());
            return new ResponseEntity<CustomerControllerException>(cce, HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            CustomerControllerException cce = new CustomerControllerException("709","Customer id dosen't exist in database");
            return new ResponseEntity<CustomerControllerException>(cce, HttpStatus.BAD_REQUEST);
        }
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
