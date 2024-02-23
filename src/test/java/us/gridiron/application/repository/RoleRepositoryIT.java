package us.gridiron.application.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.gridiron.application.models.ERole;
import us.gridiron.application.models.Role;

@SpringBootTest
@Transactional
public class RoleRepositoryIT
{
	private final RoleRepository roleRepository;

	@Autowired
	public RoleRepositoryIT(RoleRepository roleRepository)
	{
		this.roleRepository = roleRepository;
	}

	@Test
	void findByNameReturnsExpectedResult()
	{
		Role result = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();

		assertThat(result.getName(), is(ERole.ROLE_USER));
	}
}
