package com.mesbahi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserRegisteredDTO {

	
    private String name;
	
	private String email_id;
	
	private String password;
	
	private String role;

	public UserRegisteredDTO(String role) {
		super();
		this.role = role;
	}
	
	
 
}
