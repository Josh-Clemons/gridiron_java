package us.gridiron.application.services;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import us.gridiron.application.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import us.gridiron.application.repository.CompetitorRepository;
import us.gridiron.application.repository.TeamRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class NflDataService {

    private final CompetitorRepository competitorRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public NflDataService(CompetitorRepository competitorRepository, TeamRepository teamRepository) {
        this.competitorRepository = competitorRepository;
        this.teamRepository = teamRepository;
    }

    public List<Competitor> getAllGameData() throws Exception {
        List<Competitor> allCompetitors = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();


        // Configuring the ObjectMapper to parse the date string into LocalDateTime
        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        for(int i = 1; i <= 17; i++) {
            final String uri = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard?week=" + i;
                NflWeek result = restTemplate.getForObject(uri, NflWeek.class);
                System.out.println("Data: " + result);

                if(result != null) {
                    List<Event> events = result.getEvents();
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
        }

        return allCompetitors;
    }
}

