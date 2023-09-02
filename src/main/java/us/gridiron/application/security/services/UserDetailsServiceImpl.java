package us.gridiron.application.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.gridiron.application.models.User;
import us.gridiron.application.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		String lowerCaseUsernameOrEmail = usernameOrEmail.toLowerCase();
		User user = userRepository.findByUsername(usernameOrEmail)
			.orElseGet(() -> userRepository.findByEmail(lowerCaseUsernameOrEmail)
				.orElseThrow(
					() -> new UsernameNotFoundException("User Not Found with username or email: " + usernameOrEmail)));

		return UserDetailsImpl.build(user);
	}

}