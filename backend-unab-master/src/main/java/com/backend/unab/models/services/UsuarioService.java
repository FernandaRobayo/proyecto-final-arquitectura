package com.backend.unab.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IUsuarioDao;
import com.backend.unab.models.entity.User;

@Service
public class UsuarioService implements UserDetailsService {

	private final IUsuarioDao usuarioDao;

	public UsuarioService(IUsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = usuarioDao.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				Boolean.TRUE.equals(user.getEnabled()), true, true, true, authorities);
	}

	@Transactional(readOnly = true)
	public User findEntityByUsername(String username) {
		return usuarioDao.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
	}
}
