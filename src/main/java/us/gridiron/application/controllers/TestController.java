package us.gridiron.application.controllers;

import java.io.InputStream;
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
import us.gridiron.application.models.espn.Competition;
import us.gridiron.application.models.Competitor;
import us.gridiron.application.models.espn.Event;
import us.gridiron.application.models.espn.NflWeek;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@GetMapping("/allgames")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<List<Competitor>> getAllGameData() {
		List<Competitor> allCompetitors = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		// Configuring the ObjectMapper to parse the date string into LocalDateTime
		objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new JavaTimeModule());

		InputStream testDataFile = getClass().getClassLoader().getResourceAsStream("testData.json");

		try {
			NflWeek result = objectMapper.readValue(testDataFile, NflWeek.class);
			// for debugging purposes only
			System.out.println("Data: " + result);

			if (result != null) {
				List<Event> events = result.getEvents();
				// for debugging purposes only
				System.out.println("Events: " + events);

				if (events != null) {
					for (Event event : events) {
						List<Competition> competitions = event.getCompetitions();

						if (competitions != null) {
							for (Competition competition : competitions) {
								List<Competitor> competitors = competition.getCompetitors();
								if (competitors != null) {
									allCompetitors.addAll(competitors);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error reading testData.json in NflDataController: " + e.getMessage());
			return ResponseEntity.status(500).build();
		}

		return ResponseEntity.ok(allCompetitors);
	}


}