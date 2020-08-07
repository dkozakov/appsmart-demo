package ru.appsmart.demo.dkozakov.core.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class Customer {

	String id;
	String title;
	boolean deleted;
	LocalDateTime createdAt;
	LocalDateTime modifiedAt;
}
