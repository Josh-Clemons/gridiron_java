package utils;

import us.gridiron.application.models.Competitor;
import us.gridiron.application.models.League;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;

import java.util.List;

public class TestUtils
{
    public League createLeague()
    {
        League league = new League();
        league.setLeagueName("Test League");
        league.setLeagueOwner(createUser("TestUser"));
        league.setIsPrivate(false);
        league.setMaxUsers(10);
        league.setUserCount(1);
        league.setInviteCode("12345");
        league.setIsDiscontinued(false);

        return league;
    }

    public User createUser(String username)
    {
        User user = new User();
        user.setUsername(username);
        user.setEmail("testeamil@email.com");
        user.setPassword("password");

        return user;
    }

    public Competitor createWinningCompetitor()
    {
        Competitor competitor = new Competitor();
        competitor.setWinner(true);

        return competitor;
    }

    public Competitor createLosingCompetitor()
    {
        Competitor competitor = new Competitor();
        competitor.setWinner(false);

        return competitor;
    }


    public List<Pick> createFullYearOfPicks(String username)
    {
        User user = createUser(username);
        League league = createLeague();
        Competitor winner = createWinningCompetitor();
        Competitor loser = createLosingCompetitor();

        return List.of(
                new Pick(user, league, 1, 1, winner),
                new Pick(user, league, 3, 1, winner),
                new Pick(user, league, 5, 1, winner), // 11
                new Pick(user, league, 1, 2, loser),
                new Pick(user, league, 3, 2, winner),
                new Pick(user, league, 5, 2, winner), // 8
                new Pick(user, league, 1, 3, loser),
                new Pick(user, league, 3, 3, loser),
                new Pick(user, league, 5, 3, winner), // 5
                new Pick(user, league, 1, 4, loser),
                new Pick(user, league, 3, 4, winner),
                new Pick(user, league, 5, 4, loser), // 3
                new Pick(user, league, 1, 5, winner),
                new Pick(user, league, 3, 5, loser),
                new Pick(user, league, 5, 5, loser), // 1
                new Pick(user, league, 1, 6, winner),
                new Pick(user, league, 3, 6, winner),
                new Pick(user, league, 5, 6, loser), // 4
                new Pick(user, league, 1, 7, winner),
                new Pick(user, league, 3, 7, loser),
                new Pick(user, league, 5, 7, winner), // 6
                new Pick(user, league, 1, 8, loser),
                new Pick(user, league, 3, 8, loser),
                new Pick(user, league, 5, 8, winner), // 5
                new Pick(user, league, 1, 9, winner),
                new Pick(user, league, 3, 9, winner),
                new Pick(user, league, 5, 9, winner), // 11
                new Pick(user, league, 1, 10, loser),
                new Pick(user, league, 3, 10, winner),
                new Pick(user, league, 5, 10, winner), // 8
                new Pick(user, league, 1, 11, loser),
                new Pick(user, league, 3, 11, winner),
                new Pick(user, league, 5, 11, winner), // 8
                new Pick(user, league, 1, 12, loser),
                new Pick(user, league, 3, 12, loser),
                new Pick(user, league, 5, 12, winner), // 5
                new Pick(user, league, 1, 13, winner),
                new Pick(user, league, 3, 13, winner),
                new Pick(user, league, 5, 13, winner), // 11
                new Pick(user, league, 1, 14, loser),
                new Pick(user, league, 3, 14, winner),
                new Pick(user, league, 5, 14, winner), // 8
                new Pick(user, league, 1, 15, loser),
                new Pick(user, league, 3, 15, winner),
                new Pick(user, league, 5, 15, winner), // 8
                new Pick(user, league, 1, 16, loser),
                new Pick(user, league, 3, 16, loser),
                new Pick(user, league, 5, 16, winner), // 5
                new Pick(user, league, 1, 17, loser),
                new Pick(user, league, 3, 17, winner),
                new Pick(user, league, 5, 17, winner), // 8
                new Pick(user, league, 1, 18, loser),
                new Pick(user, league, 3, 18, loser),
                new Pick(user, league, 5, 18, winner) // 5
                // Total 120
        );
    }
}
