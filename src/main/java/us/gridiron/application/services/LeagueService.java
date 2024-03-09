package us.gridiron.application.services;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.request.JoinLeagueDTO;
import us.gridiron.application.repository.LeagueRepository;
import us.gridiron.application.repository.PickRepository;

import java.util.List;

@Service
public class LeagueService
{
	private final LeagueRepository leagueRepository;
	private final PickService pickService;
	private final CodeService codeService;

	public LeagueService(
		LeagueRepository leagueRepository, PickService pickService, CodeService codeService)
	{
		this.leagueRepository = leagueRepository;
		this.pickService = pickService;
		this.codeService = codeService;
	}

	public List<League> findAvailableLeagues(User user)
	{
		return leagueRepository.findAvailableLeagues(user.getId());
	}

	public League findLeagueById(Long leagueId)
	{
		return leagueRepository.findById(leagueId)
			.orElseThrow(() -> new RuntimeException("Error finding the league"));
	}

	public League findLeagueByInviteCode(String inviteCode)
	{
		return leagueRepository.findByInviteCode(inviteCode);
	}

	public League findLeagueByLeagueName(String name)
	{
		return leagueRepository.findByLeagueName(name);
	}

	public List<League> findUsersLeagues(Long userId)
	{
		return leagueRepository.findAllByUserId(userId);
	}

	@Transactional
	public League createLeague(
		String leagueName, User leagueOwner,
		Integer maxUsers, boolean isPrivate)
	{
		String inviteCode = "";
		// confirms the invite code is unique before moving on
		while(inviteCode.equals("")) {
			String tempCode = codeService.generateCode(6).getAccessCode();
			if(leagueRepository.findByInviteCode(tempCode) == null) {
				inviteCode = tempCode;
			}
		}

		League newLeague = new League(leagueName, leagueOwner, isPrivate, maxUsers, inviteCode);
		newLeague.addUser(leagueOwner);
		League savedLeague = leagueRepository.save(newLeague);
		pickService.createPicksForNewUser(leagueOwner, savedLeague);
		return savedLeague;
	}

	@Transactional
	public ResponseEntity<String> addUserToLeague(JoinLeagueDTO joinLeagueDTO, User user)
	{
		League league = leagueRepository.findByInviteCode(joinLeagueDTO.getInviteCode());
		// not allowed to join full leagues
		if(league.getMaxUsers() <= league.getUserCount()) {
			throw new RuntimeException("League is full");
		}
		// checks if league is private and user provide a matching invite code
		if(league.getIsPrivate() && !league.getInviteCode().equals(joinLeagueDTO.getInviteCode())) {
			throw new RuntimeException("Incorrect invite code");
		}
		// TODO (Josh) check is the user was in the league previously, then just activate their old picks

		if(league.addUser(user)) {
			leagueRepository.save(league);
			pickService.createPicksForNewUser(user, league);
			return ResponseEntity.ok("User added to the league");
		} else {
			return ResponseEntity.badRequest().body("User is already in the league");
		}
	}

	@Transactional
	public void removeUserFromLeague(Long leagueId, User user)
	{

		League leagueToUpdate = leagueRepository.findById(leagueId)
			.orElseThrow(() -> new RuntimeException("Error finding the league"));

		// league owner is not allowed to leave a league, they must discontinue it,
		// at some point being able to transfer ownership should be considered
		if(leagueToUpdate.getLeagueOwner().equals(user)) {
			throw new RuntimeException("Cannot remove league owner from league");
		} else if(leagueToUpdate.getUsers().stream().noneMatch(u -> u.equals(user))) {
			throw new RuntimeException("User not in the league");
		}
		// remove players picks from the league
		pickService.discontinuePicksByUserAndLeague(user, leagueToUpdate);
		leagueToUpdate.removeUser(user);
		leagueRepository.save(leagueToUpdate);
	}

	@Transactional
	public void discontinueLeague(User user, Long leagueId) {
		League leagueToDiscontinue = leagueRepository.findById(leagueId)
				.orElseThrow(() -> new RuntimeException("Error finding the league"));

		if (user.equals(leagueToDiscontinue.getLeagueOwner())) {
			pickService.discontinuePicksByUserAndLeague(user, leagueToDiscontinue);
			leagueRepository.discontinueLeagueByLeagueId(leagueId);
		} else {
			throw new RuntimeException("Unable to delete, you are not the owner");
		}
	}
}
