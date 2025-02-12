package com.commerce.customer.entity;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {

	@Id
	private Integer id;
	private String name;
	private Integer balance;


}
