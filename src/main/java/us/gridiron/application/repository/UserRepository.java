package us.gridiron.application.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import us.gridiron.application.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	@Query("SELECT u FROM User u JOIN u.leagues l WHERE l.id = :leagueId")
	List<User> findUsersByLeagueId(@Param("leagueId") Long leagueId);
}
