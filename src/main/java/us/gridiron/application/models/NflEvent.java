package us.gridiron.application.models;

import java.util.List;

public class NflEvent {

	private String id;
	private List<Competition> competitions;

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
}
