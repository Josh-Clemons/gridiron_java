package us.gridiron.application.models;

import java.util.List;

public class WeeklyGameData {
	private String id;
	private Integer week;
	private List<NflEvent> events;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<NflEvent> getEvents() {
		return events;
	}

	public void setEvents(List<NflEvent> events) {
		this.events = events;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}
}
