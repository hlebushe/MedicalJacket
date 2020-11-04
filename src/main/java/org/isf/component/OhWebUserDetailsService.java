package org.isf.component;

import org.isf.dao.User;
import org.isf.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class OhWebUserDetailsService implements UserDetailsService {
	private final UserRepository repository;

	public OhWebUserDetailsService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByUserName(username);
		if (user == null)
			throw new UsernameNotFoundException("The user does not exist");

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getUserGroupName().getCode().toUpperCase());
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPasswd(), true,
			true, true, true, Collections.singletonList(authority));
	}
}