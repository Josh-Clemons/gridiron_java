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

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	@Override
	public String toString() {
		return "CreateLeagueRequestDTO{" +
				"leagueName='" + leagueName + '\'' +
				", maxUsers=" + maxUsers +
				", isPrivate=" + isPrivate +
				'}';
	}
}
