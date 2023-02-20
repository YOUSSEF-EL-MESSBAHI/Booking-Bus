package com.mesbahi.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.TemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.mesbahi.dto.BookingsDTO;
import com.mesbahi.dto.UserRegisteredDTO;
import com.mesbahi.entity.Bookings;
import com.mesbahi.entity.Role;
import com.mesbahi.entity.User;
import com.mesbahi.repository.BookingsRepository;
import com.mesbahi.repository.RoleRepository;
import com.mesbahi.repository.UserRepository;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class DefaultUserServiceImpl implements DefaultUserService {
	@Autowired
	private UserRepository userRepo;
   
   @Autowired
	private BookingsRepository bookingRepository;
	
   @Autowired
  	private RoleRepository roleRepo;
  	
   @Autowired
   private TemplateEngine templateEngine;
   
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		User user = userRepo.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));		
	}
	
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}

	@Override
	public User save(UserRegisteredDTO userRegisteredDTO) {
		Role role = new Role();
		if(userRegisteredDTO.getRole().equals("USER"))
		  role = roleRepo.findByRole("USER");
		else if(userRegisteredDTO.getRole().equals("ADMIN"))
		 role = roleRepo.findByRole("ADMIN");
		User user = new User();
		user.setEmail(userRegisteredDTO.getEmail_id());
		user.setName(userRegisteredDTO.getName());
		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
		Set<Role> roles=new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		
		return userRepo.save(user);
	}

	@Override
	public Bookings updateBookings(BookingsDTO bookingDTO,UserDetails user) {
		Bookings booking = new Bookings();
		String email = user.getUsername();
		User users = userRepo.findByEmail(email);
		booking.setBusName(bookingDTO.getBusName());
		booking.setFilterDate(bookingDTO.getFilterDate());
		booking.setFromDestination(bookingDTO.getFromDestination());
		booking.setToDestination(bookingDTO.getToDestination());
		booking.setNoOfPersons(bookingDTO.getNoOfPersons());
		booking.setTotalCalculated(bookingDTO.getTotalCalculated());
		booking.setTime(bookingDTO.getTime());
		booking.setUserId(users.getId());
		booking.setTripStatus(true);
		String filename=generatePDFAndSendMail(bookingDTO,users);
		booking.setFileName(filename);
		return bookingRepository.save(booking);
	}

	private String generatePDFAndSendMail(BookingsDTO bookingDTO, User users) {
		
			int random = (int) (Math.random() * 90) + 10;
			String nameGenrator = users.getName()+"_ticket_"+random+".pdf";
			try {
				createPdf(bookingDTO,users ,nameGenrator);
				sendEmail(bookingDTO,users ,nameGenrator);
            return nameGenrator;
			} catch (DocumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
				}
	@Override
	public void sendEmail(BookingsDTO bookingDTO, User users, String nameGenrator) {
		try {
			final String username = "";//email id of sender
		    final String password = "";//application password of Gmail , I dont know how to generate watch this https://bit.ly/3PY4IeS

		    Properties props = new Properties();
		    props.put("mail.smtp.auth", true);
		    props.put("mail.smtp.starttls.enable", true);
		    props.put("mail.smtp.host", "smtp.gmail.com");
		    props.put("mail.smtp.port", "587");

		    Session session = Session.getInstance(props,
		            new Authenticator() {
		                protected PasswordAuthentication getPasswordAuthentication() {
		                    return new PasswordAuthentication(username, password);
		                }
		            });
		         // Create a default MimeMessage object.
		         Message message = new MimeMessage(session);

		         // Set From: header field of the header.
		         message.setFrom(new InternetAddress(username));

		         // Set To: header field of the header.
		         message.setRecipients(Message.RecipientType.TO,
		            InternetAddress.parse(users.getEmail()));

		         // Set Subject: header field
		         message.setSubject("Testing Subject");

		         // Create the message part
		         BodyPart messageBodyPart = new MimeBodyPart();

		         // Now set the actual message
		         messageBodyPart.setText("This is message body");

		         // Create a multipar message
		         Multipart multipart = new MimeMultipart();

		         // Set text message part
		         multipart.addBodyPart(messageBodyPart);

		         // Part two is attachment
		         messageBodyPart = new MimeBodyPart();
		         String filename = ""+nameGenrator+"";//directory in which pdf is created
		         DataSource source = new FileDataSource(filename);
		         messageBodyPart.setDataHandler(new DataHandler(source));
		         messageBodyPart.setFileName(filename);
		         multipart.addBodyPart(messageBodyPart);

		         // Send the complete message parts
		         message.setContent(multipart);
		         
		         Transport.send(message);

		         System.out.println("Sent message successfully....");
		         

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

	public void createPdf(BookingsDTO booking,User users,String nameGenrator) throws DocumentException, IOException {
		org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
		context.setVariable("name", users.getName());
		context.setVariable("date", booking.getFilterDate());
		context.setVariable("noOfPass", booking.getNoOfPersons());
		context.setVariable("From", booking.getFromDestination());
		context.setVariable("to", booking.getToDestination());
		context.setVariable("busName", booking.getBusName());
	
		String processHTML = templateEngine.process("template", context);
		
		try {
			java.io.OutputStream out = new FileOutputStream(""+nameGenrator+"");//directory in which you have to generate pdf of Ticket
			ITextRenderer ir = new ITextRenderer();
			ir.setDocumentFromString(processHTML);
			ir.layout();
			ir.createPDF(out, false);
			ir.finishPDF();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
