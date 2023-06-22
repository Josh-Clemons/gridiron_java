package us.gridiron.application.models.espn;

import java.util.List;

public class Event {

	private String id;
	private List<Competition> competitions;
	private String date;
	private Week week;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Competition> getCompetitions() {
		return competitions;
	}

	public void setCompetitions(List<Competition> competitions) {
		this.competitions = competitions;
	}

	public String  getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Week getWeek() {
		return week;
	}

	public void setWeek(Week week) {
		this.week = week;
	}
}