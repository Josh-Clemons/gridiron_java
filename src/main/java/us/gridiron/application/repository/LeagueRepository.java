package us.gridiron.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import us.gridiron.application.models.League;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long>
{
	League findByLeagueName(String name);

	League findByInviteCode(String inviteCode);

	@Query("SELECT l FROM League l JOIN l.users u WHERE u.id = :userId AND l.discontinued = false")
	List<League> findAllByUserId(@Param("userId") Long userId);

	// Excludes leagues that the user is already in
	@Query("SELECT l FROM League l WHERE l.userCount < l.maxUsers AND l.isPrivate = false AND l NOT IN (SELECT ll FROM League ll JOIN ll.users u WHERE u.id = :userId) AND l.discontinued = false")
	List<League> findAvailableLeagues(@Param("userId") Long userId);

	@Modifying
	@Query("UPDATE League l SET l.discontinued = true WHERE l.id = :leagueId")
	void discontinueLeagueByLeagueId(@Param("leagueId") Long leagueId);
}
