package ru.appsmart.demo.dkozakov.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.appsmart.demo.dkozakov.BaseTests;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTests extends BaseTests {
	@Autowired
	private ITokenProvider tokenProvider;


	@Test
	public void testGenerateToken() {
		User user = new User("demo", "demo", List.of(new SimpleGrantedAuthority("USER")));
		String token = tokenProvider.generateToken(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
		assertNotNull(token);
	}

	@Test
	public void testValidateToken() {
		User user = new User("demo", "demo", List.of(new SimpleGrantedAuthority("USER")));
		String token = tokenProvider.generateToken(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
		assertNotNull(token);

		boolean validateTokenResult = tokenProvider.validateToken(token);
		assertTrue(validateTokenResult);
	}

	@Test
	public void testParseToken() {

		String userName = "demo";
		String userPassword = "demopwd";
		String role = "USER";
		User user = new User(userName, userPassword, List.of(new SimpleGrantedAuthority(role)));
		String token = tokenProvider.generateToken(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
		assertNotNull(token);

		Authentication authentication = tokenProvider.parseToken(token);
		assertNotNull(authentication);
		assertEquals(userName, authentication.getName());
		assertEquals("", authentication.getCredentials());
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		assertNotNull(authorities);
		Optional<? extends GrantedAuthority> authority = authorities.stream().findFirst();
		assertFalse(authority.isEmpty());
		assertEquals(role, authority.get().getAuthority());
	}
}
