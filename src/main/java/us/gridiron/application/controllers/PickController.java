package us.gridiron.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.response.PickDTO;
import us.gridiron.application.services.PickService;
import us.gridiron.application.services.UserService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pick")
public class PickController {

    private final PickService pickService;
    private final UserService userService;

    public PickController(PickService pickService, UserService userService) {
        this.pickService = pickService;
        this.userService = userService;
    }

    @GetMapping("/my-league-picks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> findMyLeaguePicks(@RequestParam Long leagueId) {

        try{
            User user = userService.getLoggedInUser();
            List<PickDTO> picks = pickService.findPicksByUserAndLeagueId(user, leagueId);
            return ResponseEntity.ok(picks);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
