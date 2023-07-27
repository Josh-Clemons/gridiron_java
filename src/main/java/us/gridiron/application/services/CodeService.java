package us.gridiron.application.services;

import org.springframework.stereotype.Service;
import us.gridiron.application.models.Code;
import us.gridiron.application.repository.CodeRepository;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class CodeService {

	private final CodeRepository codeRepository;

	public CodeService(CodeRepository codeRepository)
	{
		this.codeRepository = codeRepository;
	}

	public Code generateCode(int codeLength, String email)
	{
		Random random = new SecureRandom();
		String alphanumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

		StringBuilder codeBuilder = new StringBuilder(codeLength);
		for(int i = 0; i < codeLength; i++) {
			codeBuilder.append(alphanumeric.charAt(random.nextInt(alphanumeric.length())));
		}

		return codeRepository.save(new Code(codeBuilder.toString(), LocalDateTime.now(), email));
	}

	public Code generateCode(int codeLength)
	{
		return generateCode(codeLength, "");
	}

	public void validateCode(String accessCode, long minutes, String email)
	{
		Code code = codeRepository.findCodeByAccessCode(accessCode);

		if(code == null) {
			throw new RuntimeException("Unable to locate code details");
		} else if(!code.getEmail().equals("") && !code.getEmail().equals(email)) {
			throw new RuntimeException("Access code does not match email");
		} else if(code.getIsUsed()) {
			throw new RuntimeException("Code already used, request another");
		} else {
			LocalDateTime now = LocalDateTime.now();
			Duration duration = Duration.between(code.getCreatedDate(), now);

			if(duration.toMinutes() >= minutes) {
				throw new RuntimeException("Access code has expired");
			}
		}

		code.setIsUsed(true);
		codeRepository.save(code);
	}
}
