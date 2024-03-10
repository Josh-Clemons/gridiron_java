package us.gridiron.application.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import us.gridiron.application.models.Pick;
import us.gridiron.application.services.LeagueService;
import us.gridiron.application.services.PickService;
import utils.TestUtils;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


public class LeagueServiceTest
{
    private LeagueService leagueService;
    @Mock
    private PickService pickService;
    private final TestUtils testUtils = new TestUtils();
    private static Map<Integer, Integer> expectedScores;

    @BeforeAll
    public static void init()
    {
        expectedScores = Map.ofEntries(
            Map.entry(18, 5),
            Map.entry(17, 8),
            Map.entry(16, 5),
            Map.entry(15, 8),
            Map.entry(14, 8),
            Map.entry(13, 11),
            Map.entry(12, 5),
            Map.entry(11, 8),
            Map.entry(10, 8),
            Map.entry(9, 11),
            Map.entry(8, 5),
            Map.entry(7, 6),
            Map.entry(6, 4),
            Map.entry(5, 1),
            Map.entry(4, 3),
            Map.entry(3, 5),
            Map.entry(2, 8),
            Map.entry(1, 11)
        );
    }
    @BeforeEach
    public void setUp()
    {
        openMocks(this);
        leagueService = new LeagueService(null, pickService, null);
    }

    @Test
    public void getLeagueScoresWorks()
    {
        List<Pick> picks = testUtils.createFullYearOfPicks("TestUser");
        when(pickService.findLeaguePicksByInviteCode(anyString()))
                .thenReturn(picks);

        Map<String, Map<Integer, Integer>> leagueScores = leagueService.getLeagueScores("12345");

        assertThat(leagueScores, hasKey("TestUser"));
        assertThat(leagueScores.get("TestUser"), equalTo(expectedScores));
    }

    @Test
    public void getLeagueScoresThrowsExceptionForNoPicksFound()
    {
        when(pickService.findLeaguePicksByInviteCode(anyString()))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> leagueService.getLeagueScores("12345"));
    }

}
