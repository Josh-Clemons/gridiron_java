package us.gridiron.application.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import us.gridiron.application.models.ERole;
import us.gridiron.application.models.Role;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.request.LoginRequest;
import us.gridiron.application.payload.request.SignupRequest;
import us.gridiron.application.payload.response.JwtResponse;
import us.gridiron.application.payload.response.MessageResponse;
import us.gridiron.application.payload.response.UserDTO;
import us.gridiron.application.repository.RoleRepository;
import us.gridiron.application.repository.UserRepository;
import us.gridiron.application.security.jwt.JwtUtils;
import us.gridiron.application.security.services.UserDetailsImpl;
import us.gridiron.application.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final UserService userService;
	private final ModelMapper modelMapper;
	AuthenticationManager authenticationManager;
	UserRepository userRepository;
	RoleRepository roleRepository;
	PasswordEncoder encoder;
	JwtUtils jwtUtils;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
						  RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, UserService userService, ModelMapper modelMapper){
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/current")
	public ResponseEntity<?> getCurrentUser(){
		try {
			User user = userService.getLoggedInUser();
			UserDTO userDTO = modelMapper.map(user, UserDTO.class);
			return ResponseEntity.ok(userDTO);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		logger.info("Post api/auth/signin, user: {}", loginRequest.getUsername());
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(
							loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());

			return ResponseEntity
					.ok(new JwtResponse(jwt,
							userDetails.getId(), userDetails.getUsername(),
							userDetails.getEmail(), roles));
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		logger.info("Post /api/auth/signup, signupRequest: {}", signUpRequest.toString());
		try {
			if (userRepository.existsByUsername(signUpRequest.getUsername())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
			}

			if (userRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use!"));
			}

			// Create new user's account
			User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

			Set<String> strRoles = signUpRequest.getRole();
			Set<Role> roles = new HashSet<>();

			if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Role is not found."));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch(role) {
						case "admin" -> {
							Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Admin role is not found."));
							roles.add(adminRole);
						}
						case "mod" -> {
							Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
								.orElseThrow(() -> new RuntimeException("Mod role is not found."));
							roles.add(modRole);
						}
						default -> {
							Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("User role is not found."));
							roles.add(userRole);
						}
					}
				});
			}

			user.setRoles(roles);

			return ResponseEntity.ok(userRepository.save(user));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}