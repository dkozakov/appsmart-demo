package ru.appsmart.demo.dkozakov.auth;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.appsmart.demo.dkozakov.auth.ITokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class TokenAuthorizationFilter extends OncePerRequestFilter {

	public static final String PREFIX = "Bearer ";

	private final ITokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

		if (!validateAuthorizationHeader(authorizationHeader)) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
		String token = StringUtils.substring(authorizationHeader, PREFIX.length());
		if (!tokenProvider.validateToken(token)) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		Authentication authentication = tokenProvider.parseToken(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(httpServletRequest, httpServletResponse);

	}

	private boolean validateAuthorizationHeader(String authorizationHeader) {
		return authorizationHeader != null
				&& authorizationHeader.startsWith(PREFIX);
	}

}
