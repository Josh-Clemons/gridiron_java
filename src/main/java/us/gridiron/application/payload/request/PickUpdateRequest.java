package us.gridiron.application.payload.request;

public class PickUpdateRequest {

	private Long id;
	private Long ownerId;
	private Long leagueId;
	private String team;
	private Integer week;
	private Integer value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PickUpdateRequest{" +
			"id=" + id +
			", leagueId=" + leagueId +
			", team='" + team + '\'' +
			", week=" + week +
			", value=" + value +
			'}';
	}
}
