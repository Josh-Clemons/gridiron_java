package us.gridiron.application.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import us.gridiron.application.models.User;
import us.gridiron.application.payload.response.UserDTO;
import us.gridiron.application.services.LeagueService;
import us.gridiron.application.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController
{
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final LeagueService leagueService;
	private final UserService userService;
	private final ModelMapper modelMapper;

	public UserController(UserService userService, ModelMapper modelMapper, LeagueService leagueService)
	{
		this.leagueService = leagueService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/current")
	public ResponseEntity<?> getCurrentUser()
	{
		logger.info("Get /user/current");
		try {
			User user = userService.getLoggedInUser();
			UserDTO userDTO = modelMapper.map(user, UserDTO.class);
			return ResponseEntity.ok(userDTO);
		} catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}


	@GetMapping("/users-by-league-id")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getUsersByLeagueId(@RequestParam Long leagueId)
	{
		logger.info("Get /user/users-by-league-id, leagueId: {}", leagueId);
		try {
			List<User> users = userService.findUsersByLeagueId(leagueId);
			return ResponseEntity.ok(users.stream().map(user -> modelMapper.map(user, UserDTO.class)));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error getting users from league: " + leagueId);
		}
	}

}
