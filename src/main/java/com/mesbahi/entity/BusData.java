package com.mesbahi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "Reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    private String filterDate;
	
	private String toDestination;
	
	private String fromDestination;
	
	private Double price; 
	
	private String BusName;
	
	private String time;

}
