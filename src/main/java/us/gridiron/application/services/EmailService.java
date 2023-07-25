package us.gridiron.application.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;
import us.gridiron.application.models.Code;
import us.gridiron.application.models.User;
import us.gridiron.application.repository.UserRepository;

@Service
public class EmailService {

	private final JavaMailSender emailSender;
	private final SpringTemplateEngine templateEngine;
	private final UserService userService;
	private final UserRepository userRepository;
	private final CodeService codeService;

	@Autowired
	public EmailService(
		JavaMailSender emailSender, SpringTemplateEngine templateEngine, UserService userService,
		UserRepository userRepository, CodeService codeService) {
		this.emailSender = emailSender;
		this.templateEngine = templateEngine;
		this.userService = userService;
		this.userRepository = userRepository;
		this.codeService = codeService;
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

	public void sendPasswordReset(String email) throws MessagingException {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("User not found"));
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(
			message, true);

		helper.setFrom("gridironpickem@outlook.com");
		helper.setTo(email);
		helper.setSubject("Password Reset: Grid Iron Pickems");

		Code code = codeService.generateCode(10);
		String codeLink = "https://gridiron-java-c95bfe4c87da.herokuapp.com/api/auth/reset?accessCode=" + code.getAccessCode();

		String content = ("<h2>" + user.getUsername() + " below is a link to reset your password</h2>" +
			"<h3>Link to league: <a href=" + codeLink + ">Click here</a></h3>");

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
