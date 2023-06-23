package us.gridiron.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;
import us.gridiron.application.repository.UserRepository;
import us.gridiron.application.services.LeagueService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/league")
public class LeagueController {

    private final LeagueService leagueService;
    private final UserRepository userRepository;

    public LeagueController(LeagueService leagueService, UserRepository userRepository) {
        this.leagueService = leagueService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all-leagues")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<League> getAllLeagues() {
        return leagueService.getAllLeagues();
    }

    @PostMapping("/create-league")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> createLeague(
            @RequestBody String leagueName,
            @RequestBody Integer maxUsers,
            @RequestBody Boolean isPrivate,
            @RequestBody String inviteCode,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {

            User loggedInUser = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: "
                            + userDetails.getUsername()));
            League newLeague = leagueService.createLeague(leagueName, loggedInUser, maxUsers, isPrivate, inviteCode);
            return ResponseEntity.ok(
                    "League created with name: " + newLeague.getLeagueName()
                            + ", and id: " + newLeague.getInviteCode());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
