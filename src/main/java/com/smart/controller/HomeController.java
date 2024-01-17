package com.smart.controller;


import java.util.Random;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;



@Controller
public class HomeController {
	
	Random random =new Random(1111);
	
	@Autowired
	private EmailService emailService;
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home - Smart Contact Manager");
		return "index";
	}
	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title","Register - Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	//handler for register user
	@RequestMapping(value="/do_register" , method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,
			BindingResult result1,
			@RequestParam(value = "agreement", 
			defaultValue = "false")Boolean agreement,
			Model model, 
			HttpSession session)
	{
		try {
			if(!agreement)
			{
				System.out.println("You have not agreed the term and conditions");
				throw new Exception("You have not agreed the term and conditions");
			}
			if(result1.hasErrors())
			{
				System.out.println("ERROR "+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnable(true);
			user.setImageUrl("user.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.println("Agreement "+agreement);
			System.out.println("User "+user);
			User result = this.userRepository.save(user);
			
			
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully register","alert-success"));
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("Something went wrong! "+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	}
	@GetMapping("/signin")
	public String customeLogin(Model model)
	{
		model.addAttribute("title","Login - Smart Contact Manager");
		return "login";
	}
	@RequestMapping("/forgot")
	public String openEmailForm(Model model)
	{
		model.addAttribute("title","Forgot - Smart Contact Manager");
		return "forgot_email_form";
		
	}
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, Model model,HttpSession session)
	{
		System.out.println("Email "+email);
		model.addAttribute("title","Verify OTP - Smart Contact Manager");
		//generating otp of 4 digit
		
		int otp = random.nextInt(999999);
		System.out.println("OTP "+otp);
		
		String subject="OTP From SCM";
		String message=""
				+"<div style='border:1px solid #e2e2e2; padding:20px'>"
				+"OTP is "
				+"<h1>"
				+"<b>"+otp
				+ "</h1>"
				+"</div>";
		String to=email;
		boolean flag = this.emailService.sendEmail(subject, message, email);
		User user = this.userRepository.getUserByUserName(email);
		if(user==null)
		{
			session.setAttribute("message", "User does not exits with this email !!");
			
			return "forgot_email_form";
		}
		else {
			
		
		if(flag)
		{
			session.setAttribute("myotp", otp);
			session.setAttribute("email",email);
			return "verify_otp";
			
		}else
		{
			session.setAttribute("message", "Check your email id !!");
			
			return "forgot_email_form";
		}
		}
		
	}
	//verify-otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session)
	{
		int myOtp=(int)session.getAttribute("myotp");
		String email=(String)session.getAttribute("email");
		if(myOtp==otp)
		{
			//password change form
			User userr = this.userRepository.getUserByUserName(email);
			if(userr==null)
			{
				//send error message
				session.setAttribute("message", "User does not exits with this email !!");
				
				return "forgot_email_form";
			}
			else
			{
				//send change password form
				
				 
			}
			
			return "password_change_form";
		}else
		{
			session.setAttribute("message", "You have enter wrong otp");
			return "verify_otp";
		}
		
		
	}
	@PostMapping("change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session)
	{
		String email=(String)session.getAttribute("email");
		User user1 = this.userRepository.getUserByUserName(email);
		user1.setPassword(this.passwordEncoder.encode(newpassword));
		this.userRepository.save(user1);
		return "redirect:/signin?change=password changed successfully..";
		
	}
	
}




























