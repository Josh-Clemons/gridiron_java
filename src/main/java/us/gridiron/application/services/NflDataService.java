package us.gridiron.application.services;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
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
public class NflDataService
{

	private final CompetitorRepository competitorRepository;
	private final TeamRepository teamRepository;
	private final ModelMapper modelMapper;

	public NflDataService(
		CompetitorRepository competitorRepository, TeamRepository teamRepository, ModelMapper modelMapper)
	{
		this.competitorRepository = competitorRepository;
		this.teamRepository = teamRepository;
		this.modelMapper = modelMapper;
	}

	public List<CompetitorDTO> getAllCompetitorData()
	{
		List<Competitor> competitors = competitorRepository.findAll();
		return competitors.stream()
			.map(competitor -> modelMapper.map(competitor, CompetitorDTO.class))
			.toList();
	}

	public List<CompetitorDTO> updateGameData()
	{
		Pair<List<CompetitorDTO>, Set<TeamDTO>> results = getAllEspnData();

		List<Team> updatedTeams = validateTeams(results);

		List<Competitor> newCompetitors = results.getFirst().stream()
			.map(competitorDTO -> modelMapper.map(competitorDTO, Competitor.class))
			.toList();

		List<Competitor> oldCompetitors = competitorRepository.findAll();

		newCompetitors.forEach(newCompetitor -> {
			oldCompetitors.stream()
				.filter(oldCompetitor -> oldCompetitor.getEventId().equals(newCompetitor.getEventId())
					&& oldCompetitor.getHomeAway().equals(newCompetitor.getHomeAway()))
				.findFirst()
				.ifPresentOrElse(oldCompetitor -> {
					oldCompetitor.setWinner(newCompetitor.isWinner());
					oldCompetitor.setStartDate(newCompetitor.getStartDate());
					oldCompetitor.setCompleted(newCompetitor.isCompleted());
					oldCompetitor.setWeek(newCompetitor.getWeek());

					competitorRepository.save(oldCompetitor);
				}, () -> {
					newCompetitor.setId(null);
					newCompetitor.setTeam(updatedTeams.stream()
						.filter(team -> team.getName().equals(newCompetitor.getTeam().getName()))
						.findFirst()
						.orElseThrow(() -> new RuntimeException("Team not found in updatedTeams list")));

					competitorRepository.save(newCompetitor);
				});
		});


		List<Competitor> savedCompetitors = competitorRepository.findAll();

		return savedCompetitors.stream()
			.map(competitor -> modelMapper.map(competitor, CompetitorDTO.class))
			.collect(Collectors.toList());
	}

	private List<Team> validateTeams(Pair<List<CompetitorDTO>, Set<TeamDTO>> results)
	{
		List<Team> newTeams = results.getSecond()
			.stream()
			.map(teamDTO -> modelMapper.map(teamDTO, Team.class))
			.toList();

		List<Team> oldTeams = teamRepository.findAll();

		if(oldTeams.isEmpty()) {
			newTeams.forEach(team -> team.setId(null));
			return teamRepository.saveAll(newTeams);
		}

		oldTeams.forEach(oldTeam -> {
			newTeams.stream()
				.filter(newTeam -> newTeam.getName().equals(oldTeam.getName()))
				.findFirst()
				.ifPresent(newTeam -> {
					oldTeam.setName(newTeam.getName());
					oldTeam.setAbbreviation(newTeam.getAbbreviation());
					oldTeam.setColor(newTeam.getColor());
					oldTeam.setAlternateColor(newTeam.getAlternateColor());
					oldTeam.setLogo(newTeam.getLogo());
				});
		});

		return teamRepository.saveAll(oldTeams);
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
			if(allTeamsDTO.isEmpty()) {
				allTeamsDTO.addAll(weeklyData.getSecond());
			}
		}
		return Pair.of(allCompetitorsDTO, allTeamsDTO);
	}

	public Pair<List<CompetitorDTO>, Set<TeamDTO>> getTemporaryEspnData()
	{
		String uri = "http://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard?limit=1000&dates=20230901-20240401";
		return fetchDataFromEspn(uri);
	}

}

