package com.mesbahi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mesbahi.dto.BookingsDTO;
import com.mesbahi.dto.ReservationDTO;
import com.mesbahi.entity.Bookings;
import com.mesbahi.entity.BusData;
import com.mesbahi.entity.User;
import com.mesbahi.mapper.ObjectCreationHelper;
import com.mesbahi.repository.BookingsRepository;
import com.mesbahi.repository.BusDataRepository;
import com.mesbahi.repository.UserRepository;
import com.mesbahi.service.DefaultUserService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	 private DefaultUserService userService;

	    public DashboardController(DefaultUserService userService) {
	        super();
	        this.userService = userService;
	    }
	
	@Autowired
	BookingsRepository bookingsRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BusDataRepository busDataRepository;

	@ModelAttribute("reservation")
    public ReservationDTO reservationDTO() {
        return new ReservationDTO();
    }
	
	@GetMapping
    public String displayDashboard(Model model){
		String user= returnUsername();
        model.addAttribute("userDetails", user);
        return "dashboard";
    }
	
	@PostMapping
	public String filterBusData( @ModelAttribute("reservation") ReservationDTO reservationDTO , Model model) {
		List<BusData> busData = busDataRepository.findByToFromAndDate(reservationDTO.getTo(), reservationDTO.getFrom(), reservationDTO.getFilterDate());
		
		
		if(busData.isEmpty()) {
			busData = null;
		}
		String user = returnUsername();
        model.addAttribute("userDetails", user);
		
		model.addAttribute("busData",busData);
		model.addAttribute("reservation", reservationDTO);
	    return "dashboard";
	}
	@GetMapping("/book/{id}")
	public String bookPage(@PathVariable int id,Model model) {
		Optional<BusData> busdata = busDataRepository.findById(id);
		BookingsDTO bks = ObjectCreationHelper.createBookingsDTO(busdata.get());
		
		String user = returnUsername();
        model.addAttribute("userDetails", user);
         
		model.addAttribute("record", bks);
	return "book";	
	}
	
	@PostMapping("/booking")
	public String finalBooking(@ModelAttribute("record") BookingsDTO bookingDTO,Model model) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
		Bookings b = userService.updateBookings(bookingDTO,user);
		model.addAttribute("record", new BookingsDTO());
		return "redirect:/myBooking";	
	}
	
	private String returnUsername() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
		User users = userRepository.findByEmail(user.getUsername());
		return users.getName();
	}
}
