package ru.appsmart.demo.dkozakov.core.model;

import lombok.Value;

@Value
public class AuthenticationRequest {
	String userName;
	String password;
}
