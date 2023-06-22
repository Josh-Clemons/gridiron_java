package us.gridiron.application.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.gridiron.application.models.Competitor;
import us.gridiron.application.models.Team;
import us.gridiron.application.services.NflDataService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gamedata")
public class NflDataController {

	private final NflDataService nflDataService;

	public NflDataController(NflDataService nflDataService) {
		this.nflDataService = nflDataService;
	}

	@GetMapping("/allgames")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<List<Competitor>> getAllGameData() {
		try{
			Pair<List<Competitor>, Set<Team>> results = nflDataService.getAllGameData();
			List<Competitor> allCompetitors = results.getFirst();
			return ResponseEntity.ok(allCompetitors);
		} catch (Exception e){
			System.out.println("Error getting /allgames data" + e.getMessage());
			return ResponseEntity.badRequest().body(new ArrayList<>());
		}
	}

	@GetMapping("/updatedb")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<String> refreshGameDataInDB() {
		try {
			nflDataService.updateGameDataInDB();
			return ResponseEntity.ok("Success updating DB");
		} catch (Exception e) {
			System.out.println(("Error getting /updatedb : " + e.getMessage()));
			return ResponseEntity.badRequest().body("Error updating DB: " + e.getMessage());
		}
	}


}
