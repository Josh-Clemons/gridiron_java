package us.gridiron.application.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;
import us.gridiron.application.repository.LeagueRepository;
import us.gridiron.application.repository.UserRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final UserRepository userRepository;

    @Autowired
    public LeagueService(LeagueRepository leagueRepository, UserRepository userRepository) {
        this.leagueRepository = leagueRepository;
        this.userRepository = userRepository;
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

    @Transactional
    public ResponseEntity<String> addUserToLeague(Long leagueId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        League league = leagueRepository.findById(leagueId)
            .orElseThrow(() -> new RuntimeException("League not found"));
        boolean isAdded = league.addUser(user);
        if(!isAdded){
            return ResponseEntity.badRequest().body("User is already in the league");
        } else {
            leagueRepository.save(league);
            return ResponseEntity.ok("User added to the league");
        }
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
