package com.example.springRest.Entity;

import lombok.Data;
@Data
public class User {  
	private Long Id;
	private String username;
	private String password;
	private String email;
	private String phoneNo;
	private String role;
}
