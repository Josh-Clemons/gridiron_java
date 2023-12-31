package us.gridiron.application.services;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
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
import us.gridiron.application.payload.response.CompetitorDTO;
import us.gridiron.application.payload.response.TeamDTO;
import us.gridiron.application.repository.CompetitorRepository;
import us.gridiron.application.repository.TeamRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NflDataService {

	private final CompetitorRepository competitorRepository;
	private final TeamRepository teamRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public NflDataService(
		CompetitorRepository competitorRepository, TeamRepository teamRepository, ModelMapper modelMapper)
	{
		this.competitorRepository = competitorRepository;
		this.teamRepository = teamRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional
	public List<CompetitorDTO> getAllCompetitorData()
	{
		List<Competitor> competitors = competitorRepository.findAll();
		return competitors.stream()
			.map(competitor -> modelMapper.map(competitor, CompetitorDTO.class))
			.toList();
	}
	
	@Transactional
	public List<CompetitorDTO> updateGameDataInDB()
	{
		Pair<List<CompetitorDTO>, Set<TeamDTO>> results = getAllEspnData();
		List<Competitor> newCompetitors = results.getFirst()
			.stream()
			.map(competitorDTO -> modelMapper.map(competitorDTO, Competitor.class))
			.toList();

		// TODO check teams in 2024 to confirm the old records will still work
		List<Team> updatedTeams = teamRepository.findAll();
		List<Competitor> oldCompetitors = competitorRepository.findAll();

		for(Competitor newCompetitor : newCompetitors) {
			boolean foundMatch = false;


			for(Competitor oldCompetitor : oldCompetitors) {

				if(oldCompetitor.getWeek().equals(newCompetitor.getWeek()) &&
					oldCompetitor.getTeam().getAbbreviation().equals(newCompetitor.getTeam().getAbbreviation())) {
					// Update the "winner" value
					oldCompetitor.setWinner(newCompetitor.isWinner());
					oldCompetitor.setEventId(newCompetitor.getEventId());
					oldCompetitor.setStartDate(newCompetitor.getStartDate());
					oldCompetitor.setCompleted(newCompetitor.isCompleted());

					for(Team team : updatedTeams) {
						if(team.getName().equals(newCompetitor.getTeam().getName())) {
							oldCompetitor.setTeam(team);
							break;
						}
					}

					foundMatch = true;
					break;
				}
			}

			if(!foundMatch) {
				for(Team team : updatedTeams) {
					if(team.getName().equals(newCompetitor.getTeam().getName())) {
						newCompetitor.setTeam(team);
						break;
					}
				}
				// Add the competitor to oldCompetitors
				oldCompetitors.add(newCompetitor);
			}
		}

		// Save the updated oldCompetitors (including modified and new competitors)
		competitorRepository.saveAll(oldCompetitors);

		return oldCompetitors.stream()
			.map(competitor -> modelMapper.map(competitor, CompetitorDTO.class))
			.collect(Collectors.toList());
	}

	private Pair<List<CompetitorDTO>, Set<TeamDTO>> fetchDataFromEspn(String uri)
	{
		List<Competitor> allCompetitors = new ArrayList<>();
		Set<Team> allTeams = new HashSet<>();
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();

		// Object mapper configurations
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new JavaTimeModule());

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
								for(Competitor competitor : competitors) {
									Team existingTeam = allTeams.stream()
										.filter(t -> t.getName().equals(competitor.getTeam().getName()))
										.findFirst()
										.orElse(null);
									if(existingTeam == null) {
										allTeams.add(competitor.getTeam());
									}
									competitor.setWeek(event.getWeek().getNumber());
									competitor.setEventId(event.getId());
									competitor.setStartDate(competition.getStartDate());
									competitor.setCompleted(competition.getStatus().getType().isCompleted());
								}
								allCompetitors.addAll(competitors);
							}
						}
					}
				}
			}
		}

		List<CompetitorDTO> allCompetitorsDTO = allCompetitors.stream()
			.map(competitor -> modelMapper.map(competitor, CompetitorDTO.class)).toList();
		Set<TeamDTO> allTeamsDTO = allTeams.stream()
			.map(team -> modelMapper.map(team, TeamDTO.class)).collect(Collectors.toSet());
		return Pair.of(allCompetitorsDTO, allTeamsDTO);
	}

	public Pair<List<CompetitorDTO>, Set<TeamDTO>> getAllEspnData()
	{
		List<CompetitorDTO> allCompetitorsDTO = new ArrayList<>();
		Set<TeamDTO> allTeamsDTO = new HashSet<>();
		for(int i = 1; i <= 18; i++) {
			String uri = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard?week=" + i;
			Pair<List<CompetitorDTO>, Set<TeamDTO>> weeklyData = fetchDataFromEspn(uri);
			allCompetitorsDTO.addAll(weeklyData.getFirst());
			allTeamsDTO.addAll(weeklyData.getSecond());
		}
		return Pair.of(allCompetitorsDTO, allTeamsDTO);
	}

	public Pair<List<CompetitorDTO>, Set<TeamDTO>> getCompetitorDataFromEspn()
	{
		String uri = "http://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard?limit=1000&dates=20230901-20240401";
		return fetchDataFromEspn(uri);
	}

}

