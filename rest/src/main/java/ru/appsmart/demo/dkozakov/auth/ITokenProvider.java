package ru.appsmart.demo.dkozakov.auth;

import org.springframework.security.core.Authentication;

/**
 *
 */
public interface ITokenProvider {


	/**
	 * Parse token
	 *
	 * @param token
	 *
	 * @return authentication
	 */
	Authentication parseToken(String token);


	/**
	 * Create token from authentication
	 *
	 * @param authentication
	 * @return token
	 */
	String generateToken(Authentication authentication);


	/**
	 * Validate token
	 *
	 * @param token
	 * @return
	 */
	boolean validateToken(String token);
}
