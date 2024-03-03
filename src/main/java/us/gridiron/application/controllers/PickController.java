package us.gridiron.application.controllers;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.request.PickUpdateRequest;
import us.gridiron.application.payload.response.PickDTO;
import us.gridiron.application.services.PickService;
import us.gridiron.application.services.UserService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pick")
public class PickController
{
	private static final Logger logger = LoggerFactory.getLogger(PickController.class);
	private final static ModelMapper modelMapper = new ModelMapper();
	private final PickService pickService;
	private final UserService userService;

	public PickController(PickService pickService, UserService userService)
	{
		this.pickService = pickService;
		this.userService = userService;
	}

	@GetMapping("/user-league-picks")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Object> findPicksByLeagueIdAndAuthenticatedUser(@RequestParam Long leagueId)
	{
		logger.info("Get /pick/user-league-picks, leagueId: {}", leagueId);
		try {
			User user = userService.getLoggedInUser();
			List<Pick> picks = pickService.findPicksByUserAndLeagueId(user, leagueId);
			List<PickDTO> pickDTOs = picks.stream()
					.map(pick -> modelMapper.map(pick, PickDTO.class))
					.toList();
			return ResponseEntity.ok(pickDTOs);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/all-league-picks")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Object> findPicksByInviteCode(@RequestParam String inviteCode)
	{
		logger.info("Get /pick/all-league-picks, inviteCode: {}", inviteCode);
		try {

			List<PickDTO> pickDTOs = pickService.findLeaguePicks(inviteCode).stream()
					.map(pick -> modelMapper.map(pick, PickDTO.class))
					.toList();

			return ResponseEntity.ok(pickDTOs);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body("error finding league picks");
		}
	}

	@PostMapping("/update")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Object> updatePicks(@RequestBody List<PickUpdateRequest> pickUpdates)
	{
		logger.info("Post /pick/update");
		try {
			User user = userService.getLoggedInUser();

			List<Pick> newPicks = pickService.convertPickUpdateRequestToPicksList(pickUpdates);
			List<Pick> updatedPicks = pickService.updateUserPicks(user, newPicks);
			List<PickDTO> pickDTOs = updatedPicks.stream()
					.map(pick -> modelMapper.map(pick, PickDTO.class))
					.toList();

			return ResponseEntity.ok(pickDTOs);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
