package com.loginPage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.loginPage.model.UserDtls;
import com.loginPage.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;

	
	@Override
	public UserDtls createuser(UserDtls user) {
	
		user.setPassword(PasswordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		return userRepository.save(user);
	}
}
