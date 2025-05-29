package com.example.springRest.Response;

import lombok.Data;

@Data
public class AdminReq {

	private String PkAdminId;
	private String name;
	private String email;
	private String address;
	private String username;
	private String password;
	private String role;
	private String contactNumber;
}
