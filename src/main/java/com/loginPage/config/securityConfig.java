package com.loginPage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class securityConfig {
	
	@Autowired
	public AuthenticationSuccessHandler customsuccessHandler;
	
	@Bean
	public UserDetailsService userdetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider getDaoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
		 daoAuthenticationProvider.setUserDetailsService(userdetailsService());
		 daoAuthenticationProvider.setPasswordEncoder(PasswordEncoder());
		return daoAuthenticationProvider;
		
	}

	/*
	 * protected void configure(AuthenticationManagerBuilder auth) throws Exception
	 * { auth.authenticationProvider(getDaoAuthenticationProvider());
	 * 
	 * }
	 * 
	 * protected void configure(HttpSecurity http) throws Exception {
	 * http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").
	 * antMatchers("/user/**").hasAnyRole("USER")
	 * .antMatchers("/**").permitAll().and().formLogin().loginPage("/signin").
	 * loginProcessingUrl("/login")
	 * .successHandler(customsuccessHandler).and().csrf().disable(); }
	 */
	
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasAnyRole("USER")
		.antMatchers("/**").permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/login")
		.successHandler(customsuccessHandler).and().csrf().disable();
		
		http.authenticationProvider(getDaoAuthenticationProvider());
        return http.build();
    }
	

}
