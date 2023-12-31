package us.gridiron.application.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Code {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String accessCode;
	private LocalDateTime createdDate;
	private boolean isUsed = false;
	private String email;

	public Code()
	{
	}

	public Code(String accessCode, LocalDateTime createdDate, String email)
	{
		this.accessCode = accessCode;
		this.createdDate = createdDate;
		this.email = email;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getAccessCode()
	{
		return accessCode;
	}

	public void setAccessCode(String accessCode)
	{
		this.accessCode = accessCode;
	}

	public LocalDateTime getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate)
	{
		this.createdDate = createdDate;
	}

	public boolean getIsUsed()
	{
		return isUsed;
	}

	public void setIsUsed(boolean isUsed)
	{
		this.isUsed = isUsed;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
}
