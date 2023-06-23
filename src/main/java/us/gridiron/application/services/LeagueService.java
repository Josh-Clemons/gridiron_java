package us.gridiron.application.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;
import us.gridiron.application.repository.LeagueRepository;

import java.util.List;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;

    @Autowired
    public LeagueService(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    public League getLeagueByLeagueName(String name){
        return leagueRepository.findByLeagueName(name);
    }

    @Transactional
    public League createLeague(String leagueName, User leagueOwner,
                               Integer maxUsers, Boolean isPrivate,
                               String inviteCode )
    {
        League newLeague = new League(leagueName, leagueOwner, isPrivate, maxUsers, inviteCode);
        newLeague.addUser(leagueOwner);
        return leagueRepository.save(newLeague);
    }
}
