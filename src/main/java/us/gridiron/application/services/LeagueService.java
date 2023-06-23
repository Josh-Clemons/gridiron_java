package us.gridiron.application.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;
import us.gridiron.application.repository.LeagueRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;

    @Autowired
    public LeagueService(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    @Transactional
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    public League getLeagueByLeagueName(String name){
        return leagueRepository.findByLeagueName(name);
    }

    @Transactional
    public League createLeague(String leagueName, User leagueOwner,
                               Integer maxUsers, boolean isPrivate)
    {
        String inviteCode = generateInviteCode();
        League newLeague = new League(leagueName, leagueOwner, isPrivate, maxUsers, inviteCode);
        newLeague.addUser(leagueOwner);
        return leagueRepository.save(newLeague);
    }

    public String generateInviteCode() {
        Random random = new SecureRandom();
        String alphanumberic = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int codeLength = 6;

        StringBuilder codeBuilder = new StringBuilder(codeLength);
        for(int i=0; i<codeLength; i++){
            codeBuilder.append(alphanumberic.charAt(random.nextInt(alphanumberic.length())));
        }
        return codeBuilder.toString();
    }
}
