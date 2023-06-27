package us.gridiron.application.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Competitor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	private Team team;
	@OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL)
	private List<Pick> picks;
	private boolean winner;
	private String homeAway;
	private String startDate;
	private int week;
	private String eventId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	public String getHomeAway() {
		return homeAway;
	}

	public void setHomeAway(String homeAway) {
		this.homeAway = homeAway;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public List<Pick> getPicks() {
		return picks;
	}

	public void setPicks(List<Pick> picks) {
		this.picks = picks;
	}
}
