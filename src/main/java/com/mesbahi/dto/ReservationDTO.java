package com.mesbahi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReservationDTO {	
	private String filterDate;
	
	private String to;
	
	private String from;

}
