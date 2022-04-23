package com.spring.service;

import com.spring.classes.MyOrders;
import com.spring.classes.Product;
import com.spring.entity.Customer;
import com.spring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    RestTemplate restTemplate;

    public Customer saveCustomer(Customer customer){
        return repository.save(customer);
    }
    public MyOrders getCustomer(int cid){
       Customer customer = repository.findById(cid).get();
       int pid = customer.getPid();
        Product product = restTemplate.getForObject("http://PRODUCTSERVICE/product/"+pid, Product.class);
        MyOrders myOrders = new MyOrders();
        myOrders.setCustomer(customer);
        myOrders.setProduct(product);
        return myOrders;
    }
    public List<Customer> getAllCustomer(){
        return repository.findAll();
    }
    public Customer updateCustomer(Customer customer, int cid){
        return repository.findById(cid).map(c ->{
            c.setCname(customer.getCname());
            c.setAddress(customer.getAddress());
            c.setEmail(customer.getEmail());
            c.setPhone(customer.getPhone());
            return repository.save(c);
        }).orElseGet(()->{
            customer.setCid(cid);
            return repository.save(customer);
        });
    }
    public Map<String, Boolean> deleteCustomer(int cid){
        repository.findById(cid).orElseThrow(()-> new RejectedExecutionException("Customer not found to delete"+cid));
        repository.deleteById(cid);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Customer Details Deleted", Boolean.TRUE);
        return response;
    }
}
