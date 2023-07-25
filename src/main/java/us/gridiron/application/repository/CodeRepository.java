package us.gridiron.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.gridiron.application.models.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    Code findCodeByAccessCode(String accessCode);
}
