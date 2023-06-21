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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gamedata")
public class NflDataController {

	@GetMapping("/allgames")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<List<Competitor>> getAllGameData() {
		List<Competitor> allCompetitors = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();


		// Configuring the ObjectMapper to parse the date string into LocalDateTime
		objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
		objectMapper.registerModule(new JavaTimeModule());

		for(int i = 1; i <= 17; i++) {
			final String uri = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard?week=" + i;
			try {
				NflWeek result = restTemplate.getForObject(uri, NflWeek.class);
				// for debugging purposes only
				System.out.println("Data: " + result);

				if(result != null) {
					List<Event> events = result.getEvents();
					// for debugging purposes only
					System.out.println("Events: " + events);

					if(events != null) {
						for(Event event : events) {
							List<Competition> competitions = event.getCompetitions();

							if(competitions != null) {
								for(Competition competition : competitions) {
									List<Competitor> competitors = competition.getCompetitors();
									if(competitors != null) {
										allCompetitors.addAll(competitors);
									}
								}
							}
						}
					}
				}
			} catch(Exception e) {
				System.out.println("Error reading testData.json in NflDataController: " + e.getMessage());
				return ResponseEntity.status(500).build();
			}
		}

		return ResponseEntity.ok(allCompetitors);
	}

}
