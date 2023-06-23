package us.gridiron.application.models;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "leagues", uniqueConstraints = {@UniqueConstraint(columnNames = "leagueName")})
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "leagues_users",
            joinColumns = @JoinColumn(name = "league_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();
    private String leagueName;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User leagueOwner;
    private Boolean isPrivate;
    private Integer maxUsers;
    private Integer userCount;
    private String inviteCode;

    public League() {
    }

    public League(String leagueName, User leagueOwner, Boolean isPrivate, Integer maxUsers, String inviteCode) {
        this.leagueName = leagueName;
        this.leagueOwner = leagueOwner;
        this.isPrivate = isPrivate;
        this.maxUsers = maxUsers;
        this.inviteCode = inviteCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
        this.userCount = this.users.size();
    }

    public void removeUser(User user) {
        this.users.remove(user);
        this.userCount = this.users.size();
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public User getLeagueOwner() {
        return leagueOwner;
    }

    public void setLeagueOwner(User leagueOwner) {
        this.leagueOwner = leagueOwner;
    }
}
