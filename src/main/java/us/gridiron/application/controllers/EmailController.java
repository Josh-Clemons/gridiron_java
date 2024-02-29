package us.gridiron.application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.gridiron.application.payload.request.HelpEmailDTO;
import us.gridiron.application.payload.request.InviteEmailDTO;
import us.gridiron.application.services.EmailService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/email")
public class EmailController
{

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final EmailService emailService;

	public EmailController(EmailService emailService)
	{
		this.emailService = emailService;
	}

	@PostMapping("/invite")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> sendTest(@RequestBody InviteEmailDTO inviteEmailDto)
	{
		logger.info("Post /email/invite");

		try {
			emailService.sendLeagueInvite(inviteEmailDto.getTo(), inviteEmailDto.getInviteCode());
			return ResponseEntity.ok("Message sent");
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/password-reset-request")
	public ResponseEntity<?> resetPasswordEmail(@RequestParam String email)
	{
		logger.info("Put /email/password-reset-request for email: {}", email);

		try {
			emailService.sendPasswordReset(email);
			return ResponseEntity.ok("Request sent");
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body("Problem sending password reset email");
		}
	}

	@PostMapping("/help")
	public ResponseEntity<String> createHelpRequest(@RequestBody HelpEmailDTO helpEmailDTO)
	{
		logger.info("Post /email/help with subject: {}", helpEmailDTO.getSubject());

		try {
			emailService.sendHelpEmail(helpEmailDTO.getEmail(), helpEmailDTO.getSubject(), helpEmailDTO.getMessage());
			return ResponseEntity.ok("Message sent");
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
