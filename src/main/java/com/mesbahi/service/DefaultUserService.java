package com.mesbahi.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.mesbahi.dto.BookingsDTO;
import com.mesbahi.dto.UserRegisteredDTO;
import com.mesbahi.entity.Bookings;
import com.mesbahi.entity.User;

public interface DefaultUserService extends UserDetailsService {
	User save(UserRegisteredDTO userRegisteredDTO);

	Bookings updateBookings(BookingsDTO bookingDTO,UserDetails user);
	
	void sendEmail(BookingsDTO bookingDTO, User users, String nameGenrator);

}
