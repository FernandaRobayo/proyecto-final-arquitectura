package com.backend.unab.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.unab.dto.AuthLoginRequestDto;
import com.backend.unab.dto.AuthLoginResponseDto;
import com.backend.unab.models.entity.User;
import com.backend.unab.models.services.UsuarioService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	private final AuthenticationManager authenticationManager;
	private final UsuarioService usuarioService;

	public AuthRestController(AuthenticationManager authenticationManager, UsuarioService usuarioService) {
		this.authenticationManager = authenticationManager;
		this.usuarioService = usuarioService;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthLoginResponseDto> login(@RequestBody AuthLoginRequestDto request) {
		if (request == null || !StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
			throw new IllegalArgumentException("Username and password are required");
		}

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername().trim(), request.getPassword()));

		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		User user = usuarioService.findEntityByUsername(request.getUsername().trim());

		String rawToken = request.getUsername().trim() + ":" + request.getPassword();
		String accessToken = "Basic " + Base64.getEncoder().encodeToString(rawToken.getBytes(StandardCharsets.UTF_8));

		AuthLoginResponseDto response = new AuthLoginResponseDto();
		response.setUsername(user.getUsername());
		response.setFullName(user.getFullName());
		response.setRoles(roles);
		response.setTokenType("Basic");
		response.setAccessToken(accessToken);

		return ResponseEntity.ok(response);
	}
}
