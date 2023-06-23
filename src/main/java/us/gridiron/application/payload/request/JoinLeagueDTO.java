package us.gridiron.application.payload.request;

public class JoinLeagueDTO {
	private Long leagueId;
	private Long userId;

	public Long getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
