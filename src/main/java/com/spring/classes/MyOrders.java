package com.spring.classes;

import com.spring.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrders {

    private Customer customer;
    private Product product;
}
