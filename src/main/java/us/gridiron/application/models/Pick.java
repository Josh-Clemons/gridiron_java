package us.gridiron.application.models;

import jakarta.persistence.*;

@Entity
public class Pick
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;
	@ManyToOne
	@JoinColumn(name = "league_id")
	private League league;
	@ManyToOne
	@JoinColumn(name = "competitor_id")
	private Competitor competitor;
	private Integer value;
	private Integer week;
	@Column(columnDefinition = "boolean default false")
	private boolean discontinued;

	public Pick()
	{
	}

	public Pick(User owner, League league, Integer value, Integer week)
	{
		this.owner = owner;
		this.league = league;
		this.value = value;
		this.week = week;
	}

	public Pick(
		Long id, User owner, League league,
		Competitor competitor, Integer value, Integer week)
	{
		this.id = id;
		this.owner = owner;
		this.league = league;
		this.competitor = competitor;
		this.value = value;
		this.week = week;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public User getOwner()
	{
		return owner;
	}

	public void setOwner(User owner)
	{
		this.owner = owner;
	}

	public League getLeague()
	{
		return league;
	}

	public void setLeague(League league)
	{
		this.league = league;
	}

	public Competitor getCompetitor()
	{
		return competitor;
	}

	public void setCompetitor(Competitor competitor)
	{
		this.competitor = competitor;
	}

	public Integer getValue()
	{
		return value;
	}

	public void setValue(Integer value)
	{
		this.value = value;
	}

	public Integer getWeek()
	{
		return week;
	}

	public void setWeek(Integer week)
	{
		this.week = week;
	}

	public void setDiscontinued(boolean discontinued)
	{
		this.discontinued = discontinued;
	}

	public boolean isDiscontinued()
	{
		return discontinued;
	}
}
