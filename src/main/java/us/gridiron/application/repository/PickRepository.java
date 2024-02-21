package us.gridiron.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.gridiron.application.models.League;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;

import java.util.List;

@Repository
public interface PickRepository extends JpaRepository<Pick, Long> {
    List<Pick> findByOwnerAndLeague(User owner, League league);
    List<Pick> findByOwnerAndLeagueId(User owner, Long leagueId);
    void deletePicksByOwnerAndLeague(User owner, League league);
    List<Pick> findPicksByLeagueInviteCode(String inviteCode);
    void deleteAllByLeague(League league);
}
