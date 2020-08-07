package ru.appsmart.demo.dkozakov.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Indicates that customer does not exist
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "customer not found")
public class CustomerNotFoundException extends RuntimeException {

	public CustomerNotFoundException(String msg) {
		super(msg);
	}
	public CustomerNotFoundException(Exception exp) {
		super(exp);
	}
}
