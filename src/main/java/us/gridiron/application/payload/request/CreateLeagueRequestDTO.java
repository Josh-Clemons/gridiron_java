package us.gridiron.application.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateLeagueRequestDTO {

	@NotBlank
	@Size(min = 3, max = 30)
	private String leagueName;

	private Integer maxUsers;

	private Boolean isPrivate;

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public Integer getMaxUsers() {
		return maxUsers == null ? 100: maxUsers;
	}

	public void setMaxUsers(Integer maxUsers) {
		this.maxUsers = maxUsers;
	}

	public Boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
}
