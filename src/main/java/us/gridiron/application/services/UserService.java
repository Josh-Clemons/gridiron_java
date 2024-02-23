package us.gridiron.application.services;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.User;
import us.gridiron.application.repository.UserRepository;

@Service
public class UserService
{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CodeService codeService;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CodeService codeService)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.codeService = codeService;
	}

	public User getLoggedInUser()
	{
		String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepository.findByUsername(loggedInUsername)
			.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + loggedInUsername));
	}

	public User resetPassword(String email, String newPassword, String accessCode)
	{
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

		codeService.validateCode(accessCode, 120, email);

		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);

		return userRepository.save(user);
	}

	/**
	 * @param password
	 * @return true if password is valid, false otherwise
	 */
	public boolean validatePassword(String password)
	{
		// This can be expanded if more password complexity is required, or better error responses
		return password.length() > 5;
	}

	public List<User> findUsersByLeagueId(Long leagueId)
	{
		return userRepository.findUsersByLeagueId(leagueId);
	}
}
