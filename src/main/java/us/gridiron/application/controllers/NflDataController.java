package us.gridiron.application.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import us.gridiron.application.models.Competition;
import us.gridiron.application.models.Competitor;
import us.gridiron.application.models.NflEvent;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gamedata")
public class NflDataController {

	@GetMapping("/allgames")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<List<Competitor>> getAllGameData() {
		List<Competitor> allCompetitors = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();

		for(int i = 1; i <= 17; i++){
			final String uri = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard?week=" + i;

			try {
				NflEvent result = restTemplate.getForObject(uri, NflEvent.class);
				// for debugging purposes only
				System.out.println("Week: " + i + ", data: " +result);
				if(result != null) {
					List<Competition> competitions = result.getCompetitions();
					// for debugging purposes only
					System.out.println("Week: " + i + ", competitions: " + competitions);
					if(competitions != null) {
						for (Competition competition : competitions) {
							List<Competitor> competitors = competition.getCompetitors();
							if (competitors != null) {
								allCompetitors.addAll(competitors);
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("error GETing espn data in GameController: " + e.getMessage());
				return ResponseEntity.status(500).build();
			}
		}

		return ResponseEntity.ok(allCompetitors);
	}

}
