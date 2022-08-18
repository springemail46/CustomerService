package com.spring.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cid;
    @NotBlank(message = "Customer name can not be blank or null")
    private String cname;
    private String address;
    @NotBlank(message = "Customer email is mandatory ")
    private String email;
    private String phone;
    private int age;
    private int pid;

}
