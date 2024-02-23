package us.gridiron.application.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.gridiron.application.models.Code;

@SpringBootTest
@Transactional
public class CodeRepositoryIT
{
	private final static LocalDateTime NOW = LocalDateTime.now();
	private final static String INVITE_CODE = "123456";
	private final static String TEST_EMAIL = "testemail";
	private final CodeRepository codeRepository;

	private Code code;

	@Autowired
	public CodeRepositoryIT(CodeRepository codeRepository)
	{
		this.codeRepository = codeRepository;
	}

	@BeforeEach
	void setUp()
	{
		code = codeRepository.save(new Code(INVITE_CODE, NOW, TEST_EMAIL));
	}

	@Test
	void findCodeByAccessCodeReturnsExpectedResults()
	{
		Code foundCode = codeRepository.findCodeByAccessCode(INVITE_CODE);

		assertThat(foundCode, is(code));
	}
}
