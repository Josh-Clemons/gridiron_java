package us.gridiron.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
	League findByLeagueName(String name);

	League findByInviteCode(String inviteCode);

	@Query("SELECT u FROM User u JOIN u.leagues l WHERE l.id = :leagueId")
	List<User> findUsersByLeagueId(@Param("leagueId") Long leagueId);

	@Query("SELECT l FROM League l JOIN l.users u WHERE u.id = :userId")
	List<League> findAllByUserId(@Param("userId") Long userId);

	@Query("SELECT l FROM League l WHERE l.userCount < l.maxUsers AND l.isPrivate = false AND l NOT IN (SELECT ll FROM League ll JOIN ll.users u WHERE u.id = :userId)")
	List<League> findAvailableLeagues(@Param("userId") Long userId);
}
