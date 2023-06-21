package us.gridiron.application.models;

import java.time.LocalDateTime;

public class Competitor {

	private String id;
	private Team team;
	private boolean winner;
	private String homeAway;
	private String startDate;
	private int week;


	public String getId() {
		return id;
	}

	public void setId(String id) {
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
}
