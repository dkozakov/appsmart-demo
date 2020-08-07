package ru.appsmart.demo.dkozakov.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.appsmart.demo.dkozakov.core.model.AuthenticationRequest;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenAuthenticationController {

	private final ITokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;


	@PostMapping("${security.get.token.uri:/auth}")
	public ResponseEntity<String> generateToken(@RequestBody AuthenticationRequest authenticateRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticateRequest.getUserName(), authenticateRequest.getPassword()));
		String token = tokenProvider.generateToken(authenticate);
		return ResponseEntity.ok(token);
	}
}
