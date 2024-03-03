package us.gridiron.application.models.espn;


import us.gridiron.application.models.Season;

import java.util.List;

public class NflWeek {

	private List<Event> events;
	private Season season;

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}
}
