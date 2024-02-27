package us.gridiron.application.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import us.gridiron.application.payload.response.CompetitorDTO;
import us.gridiron.application.payload.response.TeamDTO;
import us.gridiron.application.services.NflDataService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gamedata")
public class NflDataController
{

	public static final Logger logger = LoggerFactory.getLogger(NflDataController.class);
	private final NflDataService nflDataService;

	public NflDataController(NflDataService nflDataService)
	{
		this.nflDataService = nflDataService;
	}

	@GetMapping("/competitors")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<CompetitorDTO>> getCompetitorData()
	{
		logger.info("Get /api/gamedata/competitors");
		try {
			List<CompetitorDTO> allCompetitors = nflDataService.getAllCompetitorData();
			return ResponseEntity.ok(allCompetitors);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(new ArrayList<>());
		}
	}

	@GetMapping("/all-espn-data")
	//	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<List<CompetitorDTO>> getAllGameData()
	{
		logger.info("Get /api/gamedata/all-espn-data");
		try {
			Pair<List<CompetitorDTO>, Set<TeamDTO>> results = nflDataService.getAllEspnData();
			List<CompetitorDTO> allCompetitors = results.getFirst();
			return ResponseEntity.ok(allCompetitors);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(new ArrayList<>());
		}
	}

	@GetMapping("/espn-competitor-data")
	//	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<List<CompetitorDTO>> updateCompetitorData()
	{
		logger.info("Get /api/gamedata/espn");
		try {
			Pair<List<CompetitorDTO>, Set<TeamDTO>> results = nflDataService.getCompetitorDataFromEspn();
			List<CompetitorDTO> allCompetitors = results.getFirst();
			return ResponseEntity.ok(allCompetitors);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body(new ArrayList<>());
		}
	}

	@GetMapping("/update-db")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<?> refreshGameDataInDB()
	{
		logger.info("Get /api/gamedata/update-db");
		try {
			return ResponseEntity.ok(nflDataService.updateGameDataInDB());
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error updating DB: " + e.getMessage());
		}
	}


}
