package com.loginPage.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.loginPage.model.UserDtls;
import com.loginPage.repository.UserRepository;
import com.loginPage.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userReposi;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userReposi.findByEmail(email);
			m.addAttribute("user", user);
		}
	}

	@GetMapping("/signin")
	public String loginpage(Principal p) {
		return "login";

	}

	@GetMapping("/register")
	public String registerpage() {
		return "register";

	}

	@PostMapping("/createUser")
	public String createUser(@ModelAttribute UserDtls user, HttpSession session) {

		UserDtls createuser = userService.createuser(user);
		if (createuser != null) {
			session.setAttribute("msg", "Register Successfully");
		} else {
			session.setAttribute("msg", "Something went on server");
		}

		return "redirect:/register";
	}

	@GetMapping("/loadforgotpassword")
	public String loadforgotpassword() {
		return "forgot_password";
	}

	@GetMapping("/loadresetpassword/{id}")
	public String loadresetpassword(@PathVariable int id, Model m) {
		m.addAttribute("id" + id);
		return "reset_password";
	}

	@PostMapping("/forgotpassword")
	public String forgotpassword(@RequestParam String email, @RequestParam String mobileNum, HttpSession session) {
		UserDtls findByEmailAndMobileNumber = userReposi.findByEmailAndMobileNumber(email, mobileNum);

		if (findByEmailAndMobileNumber != null) {
			return "redirect:/loadresetpassword/" + findByEmailAndMobileNumber.getId();
		} else {
			session.setAttribute("msg", "Invalid email & Mobile Number");
			return "forgot_password";

		}

	}

	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String psw, @RequestParam Integer id, HttpSession session) {

		UserDtls user = userReposi.findById(id).get();
		String encryptpsw = passwordEncoder.encode(psw);

		user.setPassword(encryptpsw);
		UserDtls updateUser = userReposi.save(user);

		if (updateUser != null) {

			session.setAttribute("msg", "Password change successfully");
		}
		return "redirect:/loadforgotpassword";

	}
}
