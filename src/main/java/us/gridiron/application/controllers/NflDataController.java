package us.gridiron.application.controllers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import us.gridiron.application.models.Competition;
import us.gridiron.application.models.Competitor;
import us.gridiron.application.models.Event;
import us.gridiron.application.models.NflWeek;
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
			List<Competitor> allCompetitors = nflDataService.getAllGameData();
			return ResponseEntity.ok(allCompetitors);
		} catch (Exception e){
			System.out.println("Error getting /allgames data" + e.getMessage());
			return ResponseEntity.badRequest().body(new ArrayList<>());
		}
	}

}
