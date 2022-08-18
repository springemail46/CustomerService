package com.spring.service;

import com.spring.classes.MyOrders;
import com.spring.classes.Product;
import com.spring.entity.Customer;
import com.spring.exception.CustomerServiceException;
import com.spring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.RejectedExecutionException;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    RestTemplate restTemplate;

    public Customer saveCustomer(Customer customer){
        if(customer.getCname().isEmpty() || customer.getCname().length() ==0){
            throw new CustomerServiceException("701","Please enter your customer name ");
        }
        try{
            Customer saveCustomer = repository.save(customer);
            return saveCustomer;
        }catch (IllegalArgumentException ex){
            throw new CustomerServiceException("702","Given customer detils is null"+ex.getMessage());
        }catch (Exception ex){
            throw new CustomerServiceException("703","Something went wrong in service layer "+ex.getMessage());
        }
    }
    public MyOrders getCustomer(int cid){
        MyOrders myOrders = new MyOrders();
        Optional<Customer> customer=null;
        try {
             customer = repository.findById(cid);
        }catch (IllegalArgumentException ex){
            throw new CustomerServiceException("706","Given customer id is null, please provide valid customer id "+ex.getMessage());
        }catch (NoSuchElementException ex){
            throw new CustomerServiceException("707","Given Customer dose not exist in database"+ex.getMessage());
        }
       int pid = customer.get().getPid();
        Product product = restTemplate.getForObject("http://PRODUCTSERVICE/product/"+pid, Product.class);

        myOrders.setCustomer(customer.get());
        myOrders.setProduct(product);
        return myOrders;
    }
    public List<Customer> getAllCustomer(){
        List<Customer> customerList;
        try {
            customerList = repository.findAll();
            if(customerList.isEmpty()){
                throw new CustomerServiceException("704","Customer list is empty while fetching data from database ");
            }
        }catch (Exception ex){
            throw new CustomerServiceException("705","Something went worng while fetching customer details "+ex.getMessage());
        }
        return customerList;
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
