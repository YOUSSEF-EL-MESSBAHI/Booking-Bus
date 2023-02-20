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
@Table(name = "Bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int noOfPersons;

	private Double totalCalculated;

	private String filterDate;

	private String toDestination;

	private String fromDestination;

	private String BusName;

	private int userId;

	private String time;

	private String fileName;

	private boolean tripStatus;

}
