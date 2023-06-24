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

import java.util.ArrayList;
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
    public ResponseEntity<String> createLeague(
            @RequestBody CreateLeagueRequestDTO createLeagueRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {

            User loggedInUser = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: "
                            + userDetails.getUsername()));
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
    public ResponseEntity<String> joinLeague(
        @RequestBody JoinLeagueDTO joinLeagueDTO) {

        try {
            return leagueService.addUserToLeague(joinLeagueDTO.getLeagueId(), joinLeagueDTO.getUserId());
        } catch(Exception e) {
            return ResponseEntity.badRequest()
                .body("Failure to join league with leagueId: " + joinLeagueDTO.getLeagueId() + ", and userId: " + joinLeagueDTO.getUserId());
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteLeague(
            @AuthenticationPrincipal UserDetails userDetails, @RequestParam Long leagueId) {

        User loggedInUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("This seems odd, but no user found with name: "
                        + userDetails.getUsername()));

        try {
            leagueService.deleteLeague(loggedInUser.getId(), leagueId);
            return ResponseEntity.ok("Success deleting league");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
