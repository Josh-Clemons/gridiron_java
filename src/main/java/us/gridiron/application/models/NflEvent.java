package us.gridiron.application.models;

import java.util.List;

public class NflEvent {

	private String id;
	private List<CompetitorInfo> competitors;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<CompetitorInfo> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(List<CompetitorInfo> competitors) {
		this.competitors = competitors;
	}



}
