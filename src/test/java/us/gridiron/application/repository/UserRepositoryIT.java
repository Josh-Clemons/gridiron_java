package us.gridiron.application.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

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
public class UserRepositoryIT
{
	private final static String USERNAME = "testUser";
	private final static String EMAIL = "testemail@email.com";
	private final static String PASSWORD = "testPassword";

	private User user;
	private final TestEntityManager entityManager;
	private final UserRepository userRepository;
	private final LeagueRepository leagueRepository;

	@Autowired
	public UserRepositoryIT(
		EntityManagerFactory entityManagerFactory,
		UserRepository userRepository, LeagueRepository leagueRepository)
	{
		this.entityManager = new TestEntityManager(entityManagerFactory);
		this.userRepository = userRepository;
		this.leagueRepository = leagueRepository;
	}

	private User makeUser(String username, String email, String password)
	{
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		return userRepository.save(user);
	}

	private League makeLeague(String leagueName, String inviteCode, Set<User> users)
	{
		League league = new League();
		league.setLeagueName(leagueName);
		league.setInviteCode(inviteCode);
		league.setLeagueOwner(user);
		league.setMaxUsers(10);
		league.setUsers(new HashSet<>(users));
		league.setUserCount(1);
		league.setIsPrivate(false);
		return leagueRepository.save(league);
	}

	@BeforeEach
	void setUp()
	{
		entityManager.clear();
		user = makeUser(USERNAME, EMAIL, PASSWORD);
	}

	@Test
	void findByUsernameReturnsExpectedResults()
	{
		User foundUser = userRepository.findByUsername(USERNAME).orElse(null);
		assertThat(foundUser, is(user));
	}

	@Test
	void findByUsernameReturnsNullWhenNoUserExists()
	{
		User foundUser = userRepository.findByUsername("nonexistentUser").orElse(null);
		assertThat(foundUser, is((User)null));
	}

	@Test
	void existsByUsernameReturnsTrueWhenUserExists()
	{
		boolean exists = userRepository.existsByUsername(USERNAME);
		assertThat(exists, is(true));
	}

	@Test
	void existsByUsernameReturnsFalseWhenUserDoesNotExist()
	{
		boolean exists = userRepository.existsByUsername("nonexistentUser");
		assertThat(exists, is(false));
	}

	@Test
	void existsByEmailReturnsTrueWhenUserExists()
	{
		boolean exists = userRepository.existsByEmail(EMAIL);
		assertThat(exists, is(true));
	}

	@Test
	void existsByEmailReturnsFalseWhenUserDoesNotExist()
	{
		boolean exists = userRepository.existsByEmail("nonexistentEmail");
		assertThat(exists, is(false));
	}

	@Test
	void findUsersByLeagueIdReturnsExpectedResults()
	{
		User user1 = makeUser("user1", "user1@email.com", "password1");
		League league = makeLeague("league1", "inviteCode1", Set.of(user, user1));

		List<User> users = userRepository.findUsersByLeagueId(league.getId());

		assertThat(users.size(), is(2));
		assertThat(users, hasItems(user, user1));
	}
}
