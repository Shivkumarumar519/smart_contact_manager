package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	// method for adding common data
	public void addCommonData(Model m, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME " + userName);

		User user = userRepository.getUserByUserName(userName);

		System.out.println("USER " + user);
		m.addAttribute("user", user);
	}

	// home dash board
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	// open add form handler
	@GetMapping("/add-contact")
	public String openContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}

	// processing add contact form
	@PostMapping("process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {

			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading file
			if (file.isEmpty()) {
				contact.setImage("user.png");
				System.out.println("file is empty");
			} else {
				contact.setImage(file.getOriginalFilename());
				File file2 = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image to uploaded");

			}

			contact.setUser(user);

			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println("DATA " + contact);
			System.out.println("Added to database");

			// message success
			session.setAttribute("message", new Message("Your contact is added", "success"));

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();
			// message error
			session.setAttribute("message", new Message("Something went wrong", "danger"));

		}
		return "normal/add_contact_form";
	}

	// show contact handler
	// per page5[n]
	// current page 0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "Show Contacts");

		/*
		 * String name = principal.getName(); User user =
		 * this.userRepository.getUserByUserName(name); List<Contact>
		 * contacts=user.getContacts();
		 */
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		Pageable pageable = PageRequest.of(page, 5);

		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	// showing contact details
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		System.out.println("CID " + cId);
		Optional<Contact> optional = this.contactRepository.findById(cId);
		Contact contact = optional.get();

		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", "Contact detail - " + contact.getFirstName());
		} else {

		}

		return "normal/contact-detail";
	}

	// delete contact
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, Principal principal,HttpSession session) {
		
		
		//Contact contact= contactOptional.get();
		
		Contact contact = this.contactRepository.findById(cId).get();
		User user = this.userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contact);
		
		
			this.userRepository.save(user);
			
			session.setAttribute("message", new Message("Contact is deleted","success"));
			return "redirect:/user/show-contacts/0";
		
		
	}
	//open update form handler\
	@GetMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m)
	{
		m.addAttribute("title", "Update Contact");
		Contact contact = this.contactRepository.findById(cid).get();
		m.addAttribute("contact",contact);
		return "normal/update_form";
	}
	//update contact handler
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,Model m,HttpSession session,Principal principal)
	{
		
		try {
			
			//old contact details
			Contact oldContact = this.contactRepository.findById(contact.getcId()).get();
			
			
			if(!file.isEmpty())
			{
			//	delete old picture
				
				File deletefile = new ClassPathResource("static/img").getFile();
				File file1=new File(deletefile,oldContact.getImage());
				file1.delete();
				
				//update new picture
				
				File file2 = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImage(file.getOriginalFilename());
				
			}else
			{
				contact.setImage(oldContact.getImage());
			}
			User user= this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			
			session.setAttribute("message", new Message("Your contact is update","success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Contact "+contact.getFirstName());
		
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	//your profile
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title","Profile ");
		return "normal/profile";
	}
	//open setting handler
	@GetMapping("/settings")
	public String openSettings(Model model)
	{
		model.addAttribute("title", "Profile-Setting");
		return "normal/settings";
	}
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword
			,@RequestParam("newPassword") String newPassword
			,Principal principal
			,HttpSession session)
	{
		System.out.println("Old Password "+oldPassword);
		System.out.println("New Password "+newPassword);
		String userName = principal.getName();
		User currentUser = this.userRepository.getUserByUserName(userName);
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
		{
			//change the password
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message", new Message("Your password update..", "success"));
		}
		else
		{
			session.setAttribute("message", new Message("Somthing went wrong! Old password not match", "danger"));
			return  "redirect:/user/settings";
		}
		return  "redirect:/user/index";
	}
	
}












