package us.gridiron.application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.gridiron.application.payload.request.EmailDTO;
import us.gridiron.application.services.EmailService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/email")
public class EmailController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final EmailService emailService;

	@Autowired
	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping("/invite")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> sendTest(@RequestBody EmailDTO emailDto) {

		try {
			emailService.sendLeagueInvite(emailDto.getTo(), emailDto.getInviteCode());
			//			emailService.sendSimpleMessage(emailDto.getTo(), emailDto.getSubject(), emailDto.getText());
			return ResponseEntity.ok("Message sent");
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
