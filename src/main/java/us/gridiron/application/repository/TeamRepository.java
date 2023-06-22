package us.gridiron.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.gridiron.application.models.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);
}
