package com.example.springRest.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pkAdminId;
	
	private String name;
	
	private String userName;
	
	private String email;
	
	private String password;
	
	private String address;
	
	private String role;
	
	private String contactNumber;
}
