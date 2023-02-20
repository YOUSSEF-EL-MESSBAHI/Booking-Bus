package com.mesbahi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class BookingsDTO {

	private int id;
	
	private String filterDate;

	private String toDestination;

	private String fromDestination;

	private Double price;

	private String BusName;

	private String time;

	private int noOfPersons;

	private Double totalCalculated;

	private String tripStatus;
}
