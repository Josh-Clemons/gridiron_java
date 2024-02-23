package us.gridiron.application.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import us.gridiron.application.models.League;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;

@SpringBootTest
@Transactional
public class PickRepositoryIT
{
	private final static String LEAGUE_NAME = "Test League";
	private final static String INVITE_CODE = "1234";
	private final TestEntityManager testEntityManager;
	private final PickRepository pickRepository;
	private final LeagueRepository leagueRepository;
	private final UserRepository userRepository;

	private User user;
	private League league;
	private List<Pick> picks;

	@Autowired
	public PickRepositoryIT(
		EntityManagerFactory entityManagerFactory,
		PickRepository pickRepository, LeagueRepository leagueRepository,
		UserRepository userRepository)
	{
		this.testEntityManager = new TestEntityManager(entityManagerFactory);
		this.pickRepository = pickRepository;
		this.leagueRepository = leagueRepository;
		this.userRepository = userRepository;
	}

	private User makeUser(String username, String email)
	{
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("1234");
		return userRepository.save(user);
	}

	private League makeLeague(String leagueName, String inviteCode, User user)
	{
		League league = new League();
		league.setLeagueName(leagueName);
		league.setInviteCode(inviteCode);
		league.setLeagueOwner(user);
		league.setMaxUsers(10);
		league.setUsers(new HashSet<>(Set.of(user)));
		league.setUserCount(1);
		league.setIsPrivate(false);
		return leagueRepository.save(league);
	}

	private List<Pick> makePick(League league, User user)
	{
		List<Pick> newPicks = new ArrayList<>();

		for(int i = 1; i <= 18; i++) {
			newPicks.add(new Pick(user, league, 1, i));
			newPicks.add(new Pick(user, league, 3, i));
			newPicks.add(new Pick(user, league, 5, i));
		}

		return pickRepository.saveAll(newPicks);
	}

	@BeforeEach
	void setUp()
	{
		testEntityManager.clear();
		user = makeUser("test", "1234@email.com");
		league = makeLeague(LEAGUE_NAME, INVITE_CODE, user);
		picks = makePick(league, user);
	}

	@Test
	void findByOwnerAndLeagueReturnsExpectedResults()
	{
		League league2 = makeLeague("Test League 2", "5678", user);
		List<Pick> picks2 = makePick(league2, user);

		List<Pick> foundPicks = pickRepository.findByOwnerAndLeague(user, league);
		assertThat(foundPicks, is(picks));
		assertThat(foundPicks, not(picks2));
	}

	@Test
	void findByOwnerAndLeagueIdReturnsExpectedResults()
	{
		League league2 = makeLeague("Test League 2", "5678", user);
		List<Pick> picks2 = makePick(league2, user);

		List<Pick> foundPicks = pickRepository.findByOwnerAndLeagueId(user, league.getId());
		assertThat(foundPicks, is(picks));
		assertThat(foundPicks, not(picks2));
	}

	@Test
	void discontinuePicksByUserIdAndLeagueIdReturnsExpectedResults()
	{
		// this set of picks should NOT be discontinued
		League league2 = makeLeague("Test League 2", "5678", user);
		List<Pick> activePicks = makePick(league2, user);

		pickRepository.discontinuePicksByUserAndLeague(user, league);
		// refresh both sets of picks
		Stream.concat(picks.stream(), activePicks.stream()).forEach(testEntityManager::refresh);

		List<Pick> discontinuedPicks = pickRepository.findByOwnerAndLeague(user, league);
		activePicks = pickRepository.findByOwnerAndLeague(user, league2);

		assertThat(discontinuedPicks.size(), is(54));
		discontinuedPicks.forEach(pick -> assertThat(pick.isDiscontinued(), is(true)));
		activePicks.forEach(pick -> assertThat(pick.isDiscontinued(), is(false)));
	}

	@Test
	void findPicksByLeagueInviteCodeReturnsExpectedResults()
	{
		User user1 = makeUser("test1", "test1@gmail.com");
		List<Pick> createdPicks = makePick(league, user1);

		List<Pick> foundPicks = pickRepository.findByLeagueInviteCode(INVITE_CODE);

		List<Pick> allPicks = new ArrayList<>();
		allPicks.addAll(picks);
		allPicks.addAll(createdPicks);

		assertThat(foundPicks.size(), is(108));
		assertThat(foundPicks, containsInAnyOrder(allPicks.toArray()));
	}

	@Test
	void findPicksByLeagueInviteCodeDoesNotReturnDiscontinuedPicks()
	{
		pickRepository.discontinuePicksByUserAndLeague(user, league);
		picks.forEach(testEntityManager::refresh);

		List<Pick> foundPicks = pickRepository.findByLeagueInviteCode(INVITE_CODE);

		assertThat(foundPicks.size(), is(0));
	}
}
