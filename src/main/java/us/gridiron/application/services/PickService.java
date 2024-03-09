package us.gridiron.application.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.Competitor;
import us.gridiron.application.models.League;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.request.PickUpdateRequest;
import us.gridiron.application.repository.CompetitorRepository;
import us.gridiron.application.repository.LeagueRepository;
import us.gridiron.application.repository.PickRepository;
import us.gridiron.application.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PickService
{

	private static final Logger logger = LoggerFactory.getLogger(PickService.class);
	private final PickRepository pickRepository;
	private final LeagueRepository leagueRepository;
	private final CompetitorRepository competitorRepository;
	private final UserRepository userRepository;

	public PickService(
		PickRepository pickRepository, LeagueRepository leagueRepository,
		CompetitorRepository competitorRepository, UserRepository userRepository)
	{
		this.pickRepository = pickRepository;
		this.leagueRepository = leagueRepository;
		this.competitorRepository = competitorRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public void createPicksForUser(User user, League league)
	{
		// check if user has previously discontinued picks in this league
		List<Pick> picks = pickRepository.findByOwnerAndLeague(user, league);

		if(!picks.isEmpty()) {
			logger.info("User has previously discontinued picks in this league, activating picks");
			activatePicksByUserAndLeague(user, league);
			return;
		}

		// create picks for new user
		List<Pick> newPicks = new ArrayList<>();

		for(int i = 1; i <= 18; i++) {
			newPicks.add(new Pick(user, league, 1, i));
			newPicks.add(new Pick(user, league, 3, i));
			newPicks.add(new Pick(user, league, 5, i));
		}

		pickRepository.saveAll(newPicks);
	}

	@Transactional
	public List<Pick> findPicksByUserAndLeagueId(User user, Long leagueId)
	{

		// I can probably eliminate this repository call by passing in the entire league
		League league = leagueRepository.findById(leagueId)
			.orElseThrow(() -> new RuntimeException("Unable to find league"));
		boolean isLeagueMember = league.getUsers().stream().anyMatch(leagueUser -> leagueUser.equals(user));
		if(!isLeagueMember) {
			throw new RuntimeException("User not in league");
		}
		return pickRepository.findByOwnerAndLeague(user, league);
	}

	@Transactional
	public List<Pick> findLeaguePicks(String inviteCode)
	{

		return pickRepository.findByLeagueInviteCode(inviteCode);

	}

	@Transactional
	public List<Pick> updateUserPicks(User user, List<Pick> picks)
	{

		boolean isPickOwner = picks.stream()
				.allMatch(pick -> pick.getOwner().equals(user));
		boolean isLeagueOwner = leagueRepository.findById(picks.get(0).getLeague().getId())
				.map(league -> league.getLeagueOwner().equals(user))
				.orElse(false);
		if(!isLeagueOwner && !isPickOwner) {
			throw new RuntimeException("Those are not your picks!");
		}

		return pickRepository.saveAll(picks);
	}

	public void discontinuePicksByLeague(League league)
	{
		pickRepository.discontinuePicksByLeague(league);
	}

	public void discontinuePicksByUserAndLeague(User user, League league)
	{
		pickRepository.discontinuePicksByUserAndLeague(user, league);
	}

	public void activatePicksByUserAndLeague(User user, League league)
	{
		pickRepository.activatePicksByUserAndLeague(user, league);
	}

	@Transactional
	public List<Pick> convertPickUpdateRequestToPicksList(List<PickUpdateRequest> pickUpdates)
	{

		User owner = userRepository.findById(pickUpdates.get(0).getOwnerId())
			.orElseThrow(() -> new RuntimeException("User not found"));

		League league = leagueRepository.findById(pickUpdates.get(0).getLeagueId())
			.orElseThrow(() -> new RuntimeException("League not found"));

		List<Pick> updatedPicks = new ArrayList<>();
		List<Competitor> competitors = competitorRepository.findAll();

		for(PickUpdateRequest pick : pickUpdates) {
			Pick updatedPick = new Pick();
			updatedPick.setId(pick.getId());
			updatedPick.setOwner(owner);
			updatedPick.setLeague(league);
			updatedPick.setWeek(pick.getWeek());
			updatedPick.setValue(pick.getValue());

			Optional<Competitor> matchingCompetitor = competitors.stream()
				.filter(competitor -> competitor.getWeek().equals(pick.getWeek())
					&& competitor.getTeam().getAbbreviation().equals(pick.getTeam()))
				.findFirst();

			if(matchingCompetitor.isPresent()) {
				updatedPick.setCompetitor(matchingCompetitor.get());
			} else {
				updatedPick.setCompetitor(null);
			}
			updatedPicks.add(updatedPick);
		}

		return updatedPicks;
	}

}
