package us.gridiron.application.models;

import java.time.LocalDateTime;
import java.util.List;

public class Competition {

	private String id;
	private LocalDateTime startDate;
	private int week;
	private List<Competitor> competitors;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public List<Competitor> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(List<Competitor> competitors) {
		this.competitors = competitors;
	}
}
