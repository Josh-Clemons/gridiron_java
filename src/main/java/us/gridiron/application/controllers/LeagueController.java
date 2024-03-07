package us.gridiron.application.controllers;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.request.CreateLeagueRequestDTO;
import us.gridiron.application.payload.request.JoinLeagueDTO;
import us.gridiron.application.payload.response.LeagueResponseDTO;
import us.gridiron.application.payload.response.UserDTO;
import us.gridiron.application.services.LeagueService;
import us.gridiron.application.services.UserService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/league")
public class LeagueController
{

	private static final Logger logger = LoggerFactory.getLogger(LeagueController.class);
	private final LeagueService leagueService;
	private final UserService userService;
	private final ModelMapper modelMapper;

	public LeagueController(LeagueService leagueService, UserService userService, ModelMapper modelMapper)
	{
		this.leagueService = leagueService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/available")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<LeagueResponseDTO>> findAvailableLeagues()
	{
		logger.info("Get /league/available");
		User user = userService.getLoggedInUser();
		try {
			List<League> availableLeagues = leagueService.findAvailableLeagues(user);

			List<LeagueResponseDTO> availableLeaguesDTO = availableLeagues.stream()
				.map(league -> modelMapper.map(league, LeagueResponseDTO.class))
				.toList();

			return ResponseEntity.ok(availableLeaguesDTO);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(new ArrayList<>());
		}
	}

	@GetMapping("/user")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> findLeaguesByAuthenticatedUser()
	{
		logger.info("Get /league/user");
		User user = userService.getLoggedInUser();
		try {
			List<League> leagues = leagueService.findUsersLeagues(user.getId());
			List<LeagueResponseDTO> leaguesDTO = leagues.stream()
				.map(league -> modelMapper.map(league, LeagueResponseDTO.class))
				.toList();
			return ResponseEntity.ok(leaguesDTO);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error finding user's leagues");
		}
	}

	@GetMapping("/league-scores")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> findLeagueScores(@RequestParam Long leagueId)
	{
		logger.info("Get /league/league-scores, leagueId: {}", leagueId);
		try {
			return ResponseEntity.ok("good job");
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error getting league scores");
		}
	}

	@GetMapping("/find-by-id")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Object> findLeagueByLeagueId(@RequestParam Long leagueId)
	{
		logger.info("Get /league/find-by-id, leagueId: {}", leagueId);
		try {
			League league = leagueService.findLeagueById(leagueId);
			LeagueResponseDTO leagueDto = modelMapper.map(league, LeagueResponseDTO.class);
			return ResponseEntity.ok(leagueDto);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error getting league by Id: " + e.getMessage());
		}
	}

	@GetMapping("/find-by-code")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Object> findLeagueByInviteCode(@RequestParam String inviteCode)
	{
		logger.info("Get /league/find-by-code, inviteCode: {}", inviteCode);
		try {
			League league = leagueService.findLeagueByInviteCode(inviteCode);
			LeagueResponseDTO leagueDto = modelMapper.map(league, LeagueResponseDTO.class);
			return ResponseEntity.ok(leagueDto);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error getting league by Id: " + e.getMessage());
		}
	}

	@PostMapping("/create")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> createLeague(@RequestBody CreateLeagueRequestDTO createLeagueRequestDTO)
	{
		logger.info("Post /league/create, createLeagueRequestDTO: {}", createLeagueRequestDTO.toString());
		try {
			User loggedInUser = userService.getLoggedInUser();
			League newLeague = leagueService.createLeague(
				createLeagueRequestDTO.getLeagueName(), loggedInUser,
				createLeagueRequestDTO.getMaxUsers(), createLeagueRequestDTO.getIsPrivate());
			LeagueResponseDTO leagueDTO = modelMapper.map(newLeague, LeagueResponseDTO.class);
			return ResponseEntity.ok(leagueDTO);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/join")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> joinLeague(@RequestBody JoinLeagueDTO joinLeagueDTO)
	{
		logger.info("Post /league/join, joinLeagueDTO: {}", joinLeagueDTO.toString());
		try {
			User loggedInUser = userService.getLoggedInUser();
			return leagueService.addUserToLeague(joinLeagueDTO, loggedInUser);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/leave")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> leaveLeague(@RequestParam Long leagueId)
	{
		logger.info("Put /league/leave, leagueId: {}", leagueId);
		try {
			User loggedInUser = userService.getLoggedInUser();
			leagueService.removeUserFromLeague(leagueId, loggedInUser);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok("Successfully left league");
	}

	@PutMapping("/discontinue")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> discontinueLeague(@RequestParam Long leagueId)
	{
		logger.info("Put /league/delete, leagueId: {}", leagueId);
		try {
			User loggedInUser = userService.getLoggedInUser();
			leagueService.discontinueLeague(loggedInUser, leagueId);
			return ResponseEntity.ok("Success discontinuing league");
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
