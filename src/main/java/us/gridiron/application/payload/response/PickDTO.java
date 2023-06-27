package us.gridiron.application.payload.response;

public class PickDTO {
    private Long id;
    private UserDTO owner;
    private LeagueResponseDTO league;
    private CompetitorDTO competitor;
    private Integer value;
    private Integer week;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public LeagueResponseDTO getLeague() {
        return league;
    }

    public void setLeague(LeagueResponseDTO league) {
        this.league = league;
    }

    public CompetitorDTO getCompetitor() {
        return competitor;
    }

    public void setCompetitor(CompetitorDTO competitor) {
        this.competitor = competitor;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }
}
