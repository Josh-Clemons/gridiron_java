package us.gridiron.application.models;

import java.util.List;

public class Competition {

	private String id;
	private List<Competitor> competitors;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Competitor> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(List<Competitor> competitors) {
		this.competitors = competitors;
	}


}
