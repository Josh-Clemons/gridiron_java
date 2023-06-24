package us.gridiron.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import us.gridiron.application.models.League;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.request.CreateLeagueRequestDTO;
import us.gridiron.application.payload.request.JoinLeagueDTO;
import us.gridiron.application.payload.response.LeagueResponseDTO;
import us.gridiron.application.repository.UserRepository;
import us.gridiron.application.services.LeagueService;
import us.gridiron.application.services.UserService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/league")
public class LeagueController {

    private final LeagueService leagueService;
    private final UserRepository userRepository;
    private final UserService userService;

    public LeagueController(LeagueService leagueService, UserRepository userRepository, UserService userService) {
        this.leagueService = leagueService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LeagueResponseDTO>> getAllLeagues() {
        List<LeagueResponseDTO> allLeaguesDTO = new ArrayList<>();
        try {
            List<League> allLeagues = leagueService.getAllLeagues();

            for (League league : allLeagues) {
                LeagueResponseDTO leagueResponseDTO = new LeagueResponseDTO();
                leagueResponseDTO.setId(league.getId());
                leagueResponseDTO.setLeagueOwner(league.getLeagueOwner().getUsername());
                leagueResponseDTO.setUserCount(league.getUserCount());
                leagueResponseDTO.setLeagueName(league.getLeagueName());
                leagueResponseDTO.setIsPrivate(league.getIsPrivate());
                leagueResponseDTO.setMaxUsers(league.getMaxUsers());
                leagueResponseDTO.setInviteCode(league.getInviteCode());

                allLeaguesDTO.add(leagueResponseDTO);
            }
            return ResponseEntity.ok(allLeaguesDTO);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(allLeaguesDTO);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createLeague(@RequestBody CreateLeagueRequestDTO createLeagueRequestDTO) {
        try {

            User loggedInUser = userService.getLoggedInUser();
            League newLeague = leagueService.createLeague(
                createLeagueRequestDTO.getLeagueName(), loggedInUser,
                createLeagueRequestDTO.getMaxUsers(), createLeagueRequestDTO.getIsPrivate());
            return ResponseEntity.ok(
                    "League created with name: " + newLeague.getLeagueName()
                            + ", and id: " + newLeague.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> joinLeague(@RequestBody JoinLeagueDTO joinLeagueDTO) {

        User loggedInUser = userService.getLoggedInUser();

        // TODO add way to control joining private leagues
        try {
            return leagueService.addUserToLeague(joinLeagueDTO.getLeagueId(), loggedInUser);
        } catch(Exception e) {
            return ResponseEntity.badRequest()
                .body("Failure to join league with leagueId: " + joinLeagueDTO.getLeagueId() + ", and userId: " + joinLeagueDTO.getUserId());
        }
    }

    @DeleteMapping("/leave")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> leaveLeague(@RequestParam Long leagueId){

        try {
            User loggedInUser = userService.getLoggedInUser();
            leagueService.removeUserFromLeague(leagueId, loggedInUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("Successfully left league");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteLeague(@RequestParam Long leagueId) {

        User loggedInUser = userService.getLoggedInUser();

        try {
            leagueService.deleteLeague(loggedInUser, leagueId);
            return ResponseEntity.ok("Success deleting league");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
