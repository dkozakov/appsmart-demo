package ru.appsmart.demo.dkozakov.core.model;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class Product {

	String id;
	String customerId;
	String title;
	String description;
	BigDecimal price;
	boolean deleted;
	LocalDateTime createdAt;
	LocalDateTime modifiedAt;
}
