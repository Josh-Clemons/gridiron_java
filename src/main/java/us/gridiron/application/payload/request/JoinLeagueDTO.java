package us.gridiron.application.payload.request;

public class JoinLeagueDTO {
	private Long leagueId;
	private String inviteCode;

	public JoinLeagueDTO(Long leagueId, String inviteCode) {
		this.leagueId = leagueId;
		this.inviteCode = inviteCode;
	}

	public Long getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	@Override
	public String toString() {
		return "JoinLeagueDTO{" +
				"leagueId=" + leagueId +
				", inviteCode='" + inviteCode + '\'' +
				'}';
	}
}
