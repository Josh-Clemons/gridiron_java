package us.gridiron.application.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.gridiron.application.models.Competitor;
import us.gridiron.application.models.Team;
import us.gridiron.application.payload.response.CompetitorDTO;
import us.gridiron.application.payload.response.TeamDTO;
import us.gridiron.application.services.NflDataService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/gamedata")
public class NflDataController
{

	public static final Logger logger = LoggerFactory.getLogger(NflDataController.class);
	private final ModelMapper modelMapper;
	private final NflDataService nflDataService;

	public NflDataController(NflDataService nflDataService, ModelMapper modelMapper)
	{
		this.nflDataService = nflDataService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/competitors")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<CompetitorDTO>> getCompetitorData()
	{
		logger.info("Get /gamedata/competitors");
		try {
			List<Competitor> allCompetitors = nflDataService.getAllCompetitorData();
			return ResponseEntity.ok(
					allCompetitors.stream()
							.map(competitor -> modelMapper.map(competitor, CompetitorDTO.class))
							.toList()
			);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(new ArrayList<>());
		}
	}

	/**
	 * This method is used in the off-season when ESPN has not updated the detailed source of data.
	 * The data returned here is often incomplete and should only be used for off-season purposes.
	 *
	 * @return a list of CompetitorDTOs
	 */
	@GetMapping("/temporary-espn-data")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<List<CompetitorDTO>> getTemporaryEspnData()
	{
		logger.info("Get /gamedata/espn");
		try {
			Pair<List<Competitor>, Set<Team>> results = nflDataService.getTemporaryEspnData();

			List<CompetitorDTO> allCompetitorsDTO = results.getFirst().stream()
					.map(competitor -> modelMapper.map(competitor, CompetitorDTO.class))
					.toList();

			return ResponseEntity.ok(allCompetitorsDTO);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(new ArrayList<>());
		}
	}

	@GetMapping("/update-game-data")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<?> updateGameData()
	{
		logger.info("Get /gamedata/update-game-data");
		try {
			List<Competitor> competitors = nflDataService.updateGameData();

			return ResponseEntity.ok(competitors.stream()
					.map(competitor -> modelMapper.map(competitor, CompetitorDTO.class))
					.toList());
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error updating DB: " + e.getMessage());
		}
	}


}
