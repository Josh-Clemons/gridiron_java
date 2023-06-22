package us.gridiron.application.services;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import us.gridiron.application.models.Team;
import us.gridiron.application.models.espn.Competition;
import us.gridiron.application.models.Competitor;
import us.gridiron.application.models.espn.Event;
import us.gridiron.application.models.espn.NflWeek;
import us.gridiron.application.repository.CompetitorRepository;
import us.gridiron.application.repository.TeamRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NflDataService {

    private final CompetitorRepository competitorRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public NflDataService(CompetitorRepository competitorRepository, TeamRepository teamRepository) {
        this.competitorRepository = competitorRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void updateGameDataInDB() throws Exception {
        Pair<List<Competitor>, Set<Team>> results = getAllGameData();
        List<Competitor> allCompetitors = results.getFirst();
        Set<Team> allTeams = results.getSecond();
        // figure out why not all of the teams are being saved everytime, inconsistent behavior
        teamRepository.deleteAll();
        teamRepository.saveAll(allTeams);
        List<Team> updatedTeams = teamRepository.findAll();
        for (Competitor competitor : allCompetitors ){
            for (Team team : updatedTeams ) {
                if(team.getName().equals(competitor.getTeam().getName())){
                    competitor.setTeam(team);
                }
            }
        }
        competitorRepository.saveAll(allCompetitors);
    }

    public Pair<List<Competitor>, Set<Team>> getAllGameData() throws Exception {
        List<Competitor> allCompetitors = new ArrayList<>();
        Set<Team> allTeams = new HashSet<>();
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        // Object mapper configurations
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        for(int i = 1; i <= 17; i++) {
            final String uri = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard?week=" + i;
            NflWeek result = restTemplate.getForObject(uri, NflWeek.class);
            if(result != null) {
                List<Event> events = result.getEvents();
                if(events != null) {
                    for(Event event : events) {
                        List<Competition> competitions = event.getCompetitions();
                        if(competitions != null) {
                            for(Competition competition : competitions) {
                                competition.setStartDate(event.getDate());
                                List<Competitor> competitors = competition.getCompetitors();
                                if(competitors != null) {
                                    for(Competitor competitor: competitors){
                                        Team existingTeam = allTeams.stream()
                                                .filter(t -> t.getName().equals(competitor.getTeam().getName()))
                                                .findFirst()
                                                .orElse(null);
                                        if (existingTeam == null) {
                                            allTeams.add(competitor.getTeam());
                                            existingTeam = competitor.getTeam();
                                        }
                                        competitor.setTeam(existingTeam);
                                        competitor.setWeek(event.getWeek().getNumber());
                                        competitor.setEventId(event.getId());
                                        competitor.setStartDate(competition.getStartDate());
                                    }
                                    allCompetitors.addAll(competitors);
                                }
                            }
                        }
                    }
                }
            }
        }
        return  Pair.of(allCompetitors, allTeams);
    }
}

