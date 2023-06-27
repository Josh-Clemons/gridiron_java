package us.gridiron.application.payload.request;

public class JoinLeagueDTO {
	private Long leagueId;
	private Long userId;
	private String inviteCode;

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

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
}
