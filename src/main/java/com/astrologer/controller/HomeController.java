package com.astrologer.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.astrologer.dao.UserRepository;
import com.astrologer.entities.User;
import com.astrologer.helper.Message;


@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@GetMapping("")
	public String demo1() {
	
		return "login";
	}
	

	@GetMapping("/base")
	public String demo() {
	
		return "base";
	}
	
	@GetMapping("/signin")
	public String login() {
	
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup() {
	
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value="agreement", defaultValue = "false")boolean agreement, Model model, HttpSession session) {
		
		try {

			if(!agreement) {
				
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}
			
			 if(result1.hasErrors())
			 { //System.out.println("Error "+ result1.toString());
			model.addAttribute("user",user);
			 return "signup";
			 }
			 
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println("Agreement " +agreement);
			System.out.println("User" +user);
			this.userRepository.save(user);
		//	User result = this.userRepository.save(user);
		//	userRepository.save(user);
			//model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully done..","alert-success")) ;
			return "signup";
			
		} catch (Exception e) {
			
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("Something went wrong !!","alert-danger")) ;
			
		}
			
		
		return "signup";
	}
		
	
}
