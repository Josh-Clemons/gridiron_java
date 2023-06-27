package us.gridiron.application.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Pick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "picks_competitors",
            joinColumns = @JoinColumn(name = "pick_id"),
            inverseJoinColumns = @JoinColumn(name = "competitor_id"))
    private List<Competitor> competitors;

    private Integer value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public List<Competitor> getCompetitor() {
        return competitors;
    }

    public void setCompetitor(List<Competitor> competitors) {
        this.competitors = competitors;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
