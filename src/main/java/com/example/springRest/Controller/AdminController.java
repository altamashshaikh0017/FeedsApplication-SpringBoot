package com.example.springRest.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springRest.Response.AdminReq;
import com.example.springRest.Response.ResponseBean;
import com.example.springRest.Service.AdminService;

@Controller
public class AdminController {

	@Autowired
	AdminService adminService;

	@PostMapping("/signUp")
	public String addAdmin(@ModelAttribute AdminReq req, Model model) {
		System.out.println("Inside signup" + AdminController.class);
		String result = adminService.addAdmin(req);

		if (!"SUCCESS".equals(result)) {
			model.addAttribute("error", result);
			return "register";  
		}
		 model.addAttribute("message", "Registration successful! Please login.");

		if ("ADMIN".equalsIgnoreCase(req.getRole())) {
			return "redirect:/admin/register";  // admin dashboard page
//			return "redirect:/viewPosts";
		} else {
			return "redirect:/admin/register";  // user home page
		}
	}

//	@GetMapping("/register")
//	public String showRegisterForm() {
//		return "register";
//	}

}
