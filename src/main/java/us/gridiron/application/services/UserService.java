package us.gridiron.application.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.User;
import us.gridiron.application.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedInUser() {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + loggedInUsername));
    }
}
