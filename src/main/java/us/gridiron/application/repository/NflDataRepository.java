package us.gridiron.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import us.gridiron.application.models.Competitor;

public interface NflDataRepository extends JpaRepository {

	Optional<Competitor> findByTeamId(Long teamId);
	Competitor findByWeek(int week);
}
