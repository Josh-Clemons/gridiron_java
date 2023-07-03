package us.gridiron.application.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.request.JoinLeagueDTO;
import us.gridiron.application.repository.LeagueRepository;
import us.gridiron.application.repository.PickRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final PickService pickService;
    private final PickRepository pickRepository;

    @Autowired
    public LeagueService(LeagueRepository leagueRepository, PickService pickService, PickRepository pickRepository) {
        this.leagueRepository = leagueRepository;
        this.pickService = pickService;
        this.pickRepository = pickRepository;
    }

    @Transactional
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }
    public League findLeagueById(Long leagueId) {
        return leagueRepository.findById(leagueId)
                .orElseThrow(() -> new RuntimeException("Error finding the league"));
    }

    public League findLeagueByLeagueName(String name){
        return leagueRepository.findByLeagueName(name);
    }

    public List<League> findUsersLeagues(Long userId) {
        return leagueRepository.findAllByUserId(userId);
    }

    @Transactional
    public League createLeague(String leagueName, User leagueOwner,
                               Integer maxUsers, boolean isPrivate)
    {
        String inviteCode = "";
        // confirms the invite code is unique before moving on
        while (inviteCode.equals("")){
            String tempCode = generateInviteCode();
            if (leagueRepository.findByInviteCode(tempCode) == null) {
                inviteCode = tempCode;
            }
        }

        League newLeague = new League(leagueName, leagueOwner, isPrivate, maxUsers, inviteCode);
        newLeague.addUser(leagueOwner);
        return leagueRepository.save(newLeague);
    }

    @Transactional
    public ResponseEntity<String> addUserToLeague(JoinLeagueDTO joinLeagueDTO, User user) {
        League league = leagueRepository.findById(joinLeagueDTO.getLeagueId())
            .orElseThrow(() -> new RuntimeException("League not found"));
        // checks if league is private and user provide a matching invite code
        if(league.getIsPrivate() && !league.getInviteCode().equals(joinLeagueDTO.getInviteCode())){
            throw new RuntimeException("Incorrect invite code");
        }
        boolean isAdded = league.addUser(user);
        if(!isAdded){
            return ResponseEntity.badRequest().body("User is already in the league");
        } else {
            leagueRepository.save(league);
            pickService.createPicksForNewUser(user, league);
            return ResponseEntity.ok("User added to the league");
        }
    }

    @Transactional
    public void removeUserFromLeague(Long leagueId, User user) {

        League leagueToUpdate = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new RuntimeException("Error finding the league"));

        // league owner is not allowed to leave a league, they must delete it,
        // at some point being able to transfer ownership should be considered
        if(leagueToUpdate.getLeagueOwner().equals(user)){
            throw new RuntimeException("Cannot remove league owner from league");
        }else if(leagueToUpdate.getUsers().stream().noneMatch(u -> u.equals(user))){
            throw new RuntimeException("User not in the league");
        }
        // remove players picks from the league
        pickRepository.deletePicksByOwnerAndLeague(user, leagueToUpdate);
        leagueToUpdate.removeUser(user);
        leagueRepository.save(leagueToUpdate);
    }

    @Transactional
    public void deleteLeague(User user, Long leagueId) {
        League leagueToDelete = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new RuntimeException("Error finding the league"));
        if(user.equals(leagueToDelete.getLeagueOwner())){
            leagueRepository.delete(leagueToDelete);
        } else {
            throw new RuntimeException("Unable to delete, you are not the owner");
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
