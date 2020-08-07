package ru.appsmart.demo.dkozakov.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.appsmart.demo.dkozakov.auth.ITokenProvider;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider implements ITokenProvider {
	@Value("${jwt.secret.key:secret}")
	private String secretKey;

	@Value("${jwt.token.expiration:900000}")
	private int tokenExpiration;

	@PostConstruct
	protected void init() throws UnsupportedEncodingException {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes("UTF-8"));
	}

	@Override
	public Authentication parseToken(String token) {
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));;
		Jws<Claims> jwsClaims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		String username = jwsClaims.getBody().getSubject();
		List<GrantedAuthority> authorities = ((List<String>)jwsClaims.getBody().get("authorities")).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		return new UsernamePasswordAuthenticationToken(new User(username, "", authorities), "", authorities);
	}


	@Override
	public String generateToken(Authentication authentication) {
		String userName = authentication.getName();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));;
		long currentTime = System.currentTimeMillis();
		return Jwts.builder()
				.setSubject(userName)
				.claim("authorities",
						authorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(currentTime))
				.setExpiration(new Date(currentTime + tokenExpiration))
				.signWith(key).compact();
	}


	@Override
	public boolean validateToken(String token) {
		try {
			Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));;
			Jws<Claims> jwsClaims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			if (jwsClaims.getBody().getExpiration().before(new Date())) {
				return false;
			}
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
