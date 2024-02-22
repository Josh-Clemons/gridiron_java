package us.gridiron.application.controllers;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import us.gridiron.application.models.User;
import us.gridiron.application.payload.response.UserDTO;
import us.gridiron.application.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController
{
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;
	private final ModelMapper modelMapper;

	public UserController(UserService userService, ModelMapper modelMapper)
	{
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/current")
	public ResponseEntity<?> getCurrentUser()
	{
		try {
			User user = userService.getLoggedInUser();
			UserDTO userDTO = modelMapper.map(user, UserDTO.class);
			return ResponseEntity.ok(userDTO);
		} catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
