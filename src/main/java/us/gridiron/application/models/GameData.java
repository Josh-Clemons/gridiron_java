package us.gridiron.application.models;

import java.util.List;

public class GameData {
	private String id;
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

}
