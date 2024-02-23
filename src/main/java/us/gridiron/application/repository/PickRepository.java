package us.gridiron.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import us.gridiron.application.models.League;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;

import java.util.List;

@Repository
public interface PickRepository extends JpaRepository<Pick, Long>
{
	List<Pick> findByOwnerAndLeague(User owner, League league);

	List<Pick> findByOwnerAndLeagueId(User owner, Long leagueId);

	@Modifying
	@Query("UPDATE Pick p SET p.discontinued = true WHERE p.owner = :user AND p.league = :league")
	void discontinuePicksByUserAndLeague(@Param("user") User user, @Param("league") League league);

	@Query("SELECT p FROM Pick p WHERE p.league.inviteCode = :inviteCode AND p.discontinued = false")
	List<Pick> findByLeagueInviteCode(String inviteCode);
}
