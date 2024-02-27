package us.gridiron.application.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.gridiron.application.models.Competitor;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor, Long>
{
}
