package com.example.springRest.Service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springRest.Model.Admin;
import com.example.springRest.Repository.AdminRepository;
import com.example.springRest.Response.AdminReq;
import com.example.springRest.Response.ResponseBean;

@Service
public class AdminService {
	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public String addAdmin(AdminReq req) {
		
		 try {
	            if (req.getName() == null || req.getName().isEmpty() ||
	                req.getEmail() == null || req.getEmail().isEmpty() ||
	                req.getUsername() == null || req.getUsername().isEmpty() ||
	                req.getPassword() == null || req.getPassword().isEmpty() ||
	                req.getRole() == null || req.getRole().isEmpty() ||
	                req.getContactNumber() == null || req.getContactNumber().isEmpty()) {
	                return "All required fields must be filled.";
	            }

	            Admin admin = new Admin();
	            admin.setName(req.getName());
	            admin.setEmail(req.getEmail());
	            admin.setUserName(req.getUsername());
	            admin.setAddress(req.getAddress());
	            admin.setRole(req.getRole().toUpperCase());
	            admin.setContactNumber(req.getContactNumber());

	            String encodedPassword = passwordEncoder.encode(req.getPassword());
	            admin.setPassword(encodedPassword);

	            adminRepository.save(admin);
	            return "SUCCESS";

	        } catch (Exception e) {
	            e.printStackTrace();
	            return "An error occurred while adding admin.";
	        }
	    }

	public Admin findByUsername(String userName) {
		Admin admin = adminRepository.findByUserName(userName).orElse(null);
		return admin;
	}
}
