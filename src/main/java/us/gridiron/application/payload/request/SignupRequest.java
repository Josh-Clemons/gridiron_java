package us.gridiron.application.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;

public class SignupRequest {
	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	private Set<String> role;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	public String getUsername()
	{
		return username.toLowerCase();
	}

	public void setUsername(String username)
	{
		this.username = username.toLowerCase();
	}

	public String getEmail()
	{
		return email.toLowerCase();
	}

	public void setEmail(String email)
	{
		this.email = email.toLowerCase();
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Set<String> getRole()
	{
		return this.role;
	}

	public void setRole(Set<String> role)
	{
		this.role = role;
	}

	@Override
	public String toString()
	{
		return "SignupRequest{" +
			"username='" + username + '\'' +
			", email='" + email + '\'' +
			", role=" + role +
			", password='" + password + '\'' +
			'}';
	}
}