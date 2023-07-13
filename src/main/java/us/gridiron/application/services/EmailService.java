package us.gridiron.application.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;
import us.gridiron.application.models.User;

@Service
public class EmailService {

	private final JavaMailSender emailSender;
	private final SpringTemplateEngine templateEngine;
	private final UserService userService;

	@Autowired
	public EmailService(JavaMailSender emailSender, SpringTemplateEngine templateEngine, UserService userService) {
		this.emailSender = emailSender;
		this.templateEngine = templateEngine;
		this.userService = userService;
	}

	public void sendLeagueInvite(String to, String inviteCode) throws MessagingException {
		User user = userService.getLoggedInUser();
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(
			message, true);

		helper.setFrom("gridironpickem@outlook.com");
		helper.setTo(to);
		helper.setSubject("Grid Iron Pickems");

		String leagueLink = "http://localhost:5173/#/league-detail/" + inviteCode;
		String registerLink = "http://localhost:5173/#/register";

		String content = ("<h1>" + user.getUsername() + " has invited you to join a Grid Iron league!</h1>" +
			"<h3>Link to league: <a href=" + leagueLink + ">" + inviteCode + "</a></h3>" +
			"<h3>Sign up for an account <a href=" + registerLink + ">here<a/></h3>");

		helper.setText(content, true);

		emailSender.send(message);

	}

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("gridironpickem@outlook.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}
}
