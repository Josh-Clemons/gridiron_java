package us.gridiron.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.gridiron.application.models.League;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    League findByLeagueName(String name);
}
