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
import us.gridiron.application.models.CompetitorInfo;
import us.gridiron.application.models.WeeklyGameData;
import us.gridiron.application.models.NflEvent;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gamedata")
public class NflDataController {

	@GetMapping("/allgames")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<List<CompetitorInfo>> getAllGameData() {
		List<CompetitorInfo> allCompetitors = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();

		for(int i = 1; i <= 17; i++){
			final String uri = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard?week=" + i;

			try {
				WeeklyGameData result = restTemplate.getForObject(uri, WeeklyGameData.class);

				if(result != null) {
					// for debugging purposes
					System.out.println("Week: " + result.getWeek());
					for (NflEvent event : result.getEvents()) {
						// for debugging purposes
						for (CompetitorInfo competitor: event.getCompetitors()) {
							System.out.println("Event id: " + event.getId() + ", competitor: " + competitor);
						}
						allCompetitors.addAll(event.getCompetitors());
					}
				} else {
					// for debugging purposes
					System.out.println("Result is null for data from week: " + i);
				}
			} catch (Exception e) {
				System.out.println("error GETing espn data in GameController: " + e.getMessage());
				return ResponseEntity.status(500).build();
			}
		}

		return ResponseEntity.ok(allCompetitors);
	}

}
