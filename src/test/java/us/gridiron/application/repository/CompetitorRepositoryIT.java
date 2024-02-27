package us.gridiron.application.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.gridiron.application.models.Competitor;

@SpringBootTest
@Transactional
public class CompetitorRepositoryIT
{
	private final CompetitorRepository competitorRepository;

	private Competitor competitor1;

	private Competitor makeCompetitor(int week)
	{
		Competitor entity = new Competitor();

		entity.setHomeAway("home");
		entity.setWinner(true);
		entity.setWeek(week);
		entity.setCompleted(false);

		return competitorRepository.save(entity);
	}

	@Autowired
	public CompetitorRepositoryIT(CompetitorRepository competitorRepository)
	{
		this.competitorRepository = competitorRepository;
	}

	@BeforeEach
	void setUp()
	{
		competitor1 = makeCompetitor(1);
	}

	@Test
	void findAllReturnsAllCompetitors()
	{
		assertThat(competitorRepository.findAll(), not(empty()));
	}

	@Test
	void updateCustomerReturnsExpectedResult()
	{
		competitor1.setWeek(2);
		competitorRepository.save(competitor1);
		Competitor updatedCompetitor = competitorRepository.findById(competitor1.getId()).get();
		assertThat(updatedCompetitor.getWeek(), is(2));
		assertThat(competitor1.getId(), is(updatedCompetitor.getId()));

	}
}
