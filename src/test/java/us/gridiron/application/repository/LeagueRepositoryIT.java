package us.gridiron.application.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;

@SpringBootTest
@Transactional
public class LeagueRepositoryIT
{
	private final static String LEAGUE_NAME = "Test League";
	private final static String INVITE_CODE = "1234";

	private final TestEntityManager testEntityManager;
	private final LeagueRepository leagueRepository;
	private final UserRepository userRepository;


	private League league;
	private User user;

	@Autowired
	public LeagueRepositoryIT(
		LeagueRepository leagueRepository, UserRepository userRepository,
		EntityManagerFactory entityManagerFactory)
	{
		this.testEntityManager = new TestEntityManager(entityManagerFactory);
		this.leagueRepository = leagueRepository;
		this.userRepository = userRepository;
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

	private User makeUser(String username, String email)
	{
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("1234");
		return userRepository.save(user);
	}

	@BeforeEach
	void setUp()
	{
		testEntityManager.clear();
		user = makeUser("test", "1234@email.com");
		league = makeLeague(LEAGUE_NAME, INVITE_CODE, user);
	}

	@Test
	void findByLeagueNameReturnsExpectedResults()
	{
		assertThat(leagueRepository.findByLeagueName(LEAGUE_NAME), is(league));
	}

	@Test
	void findByInviteCodeReturnsExpectedResults()
	{
		assertThat(leagueRepository.findByInviteCode(INVITE_CODE), is(league));
	}

	@Test
	void findAllByUserIdReturnsExpectedResults()
	{
		makeLeague("Test League 1", "12345", user);

		assertThat(leagueRepository.findAllByUserId(user.getId()), hasSize(2));
	}

	@Test
	void findAllByUserIdReturnsExpectedResultsWhenNoLeagues()
	{
		assertThat(leagueRepository.findAllByUserId(-1L), hasSize(0));
	}

	@Test
	void findAllByUserIdReturnsExpectedResultsWithNullUserId()
	{
		assertThat(leagueRepository.findAllByUserId(null), empty());
	}

	@Test
	void findAllByUserIdDoesNotReturnDiscontinuedLeagues()
	{
		League privateLeague = makeLeague("Private League", "123456", user);
		privateLeague.setIsDiscontinued(true);
		leagueRepository.save(privateLeague);

		assertThat(leagueRepository.findAllByUserId(user.getId()), not(hasItem(privateLeague)));
	}


	@Test
	void findAvailableLeaguesReturnsExpectedResults()
	{
		League privateLeague = makeLeague("Private League", "123456", user);
		privateLeague.setIsPrivate(true);
		leagueRepository.save(privateLeague);

		List<League> leagueList = leagueRepository.findAvailableLeagues(-1L);

		assertThat(leagueList, hasItem(league));
		assertThat(leagueList, not(hasItem(privateLeague)));
	}

	@Test
	void discontinueLeagueByLeagueIdReturnsExpectedResult()
	{
		leagueRepository.discontinueLeagueByLeagueId(league.getId());

		testEntityManager.refresh(league);

		assertThat(leagueRepository.findById(league.getId()).get().isDiscontinued(), is(true));

	}
}
