package com.loginPage.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.loginPage.model.UserDtls;
import com.loginPage.repository.UserRepository;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UserRepository userReposi;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		String email = p.getName();
		UserDtls user = userReposi.findByEmail(email);

		m.addAttribute("user", user);
	}

	@GetMapping("/")
	public String home() {
		return "user/home";
	}

	@GetMapping("/changepassword")
	public String loadchangepassword() {
		return "user/changepassword";
	}

	@PostMapping("/updatepassword")
	public String changepassword(Principal p, @RequestParam("oldpass") String oldpass,
			@RequestParam("newpass") String newpass, HttpSession session) {
		String email = p.getName();

		UserDtls loginuser = userReposi.findByEmail(email);
		
		

		boolean matches = passwordEncoder.matches(oldpass, loginuser.getPassword());

		if (matches) {

			loginuser.setPassword(passwordEncoder.encode(newpass));
			UserDtls updatepass = userReposi.save(loginuser);

			if (updatepass != null) {
				session.setAttribute("msg", "Password update Successfully");
			} else {
				session.setAttribute("msg", "Something went wrong");
			}
		} else {
			session.setAttribute("msg", "Old password is incorrect");
		}

		return "redirect:/user/changepassword";

	}
}
