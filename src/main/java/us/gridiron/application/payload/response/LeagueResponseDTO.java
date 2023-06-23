package us.gridiron.application.payload.response;

public class LeagueResponseDTO {

	private Long id;
	private String leagueOwner;
	private Integer userCount;
	private String leagueName;
	private Boolean isPrivate;
	private Integer maxUsers;
	private String inviteCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLeagueOwner() {
		return leagueOwner;
	}

	public void setLeagueOwner(String leagueOwner) {
		this.leagueOwner = leagueOwner;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public Integer getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(Integer maxUsers) {
		this.maxUsers = maxUsers;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	@Override
	public String toString() {
		return "LeagueResponseDTO{" +
			"id=" + id +
			", leagueOwner='" + leagueOwner + '\'' +
			", userCount=" + userCount +
			", leagueName='" + leagueName + '\'' +
			", isPrivate=" + isPrivate +
			", maxUsers=" + maxUsers +
			", inviteCode='" + inviteCode + '\'' +
			'}';
	}
}
