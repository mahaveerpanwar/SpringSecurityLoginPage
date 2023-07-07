package com.loginPage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loginPage.model.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {

	public UserDtls findByEmail(String email);

	public UserDtls findByEmailAndMobileNumber(String em, String mobno);
}
