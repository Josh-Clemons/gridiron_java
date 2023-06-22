package us.gridiron.application.models.espn;

import us.gridiron.application.models.espn.Event;

import java.util.List;

public class NflWeek {

	private List<Event> events;

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
}
