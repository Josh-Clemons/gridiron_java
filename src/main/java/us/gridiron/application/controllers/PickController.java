package us.gridiron.application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.response.PickDTO;
import us.gridiron.application.services.PickService;
import us.gridiron.application.services.UserService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pick")
public class PickController {
    private static final Logger logger = LoggerFactory.getLogger(PickController.class);
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
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all-league-picks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> findAllLeaguePicks(@RequestParam Long leagueId) {
        try {
            return ResponseEntity.ok(pickService.findLeaguePicks(leagueId));
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body("error finding league picks");
        }
    }

    @PostMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> updateLeaguePicks(@RequestBody List<PickDTO> pickDTOS){

        try{
            User user = userService.getLoggedInUser();
            List<PickDTO> updatedPickDTOS = pickService.updateUserPicks(user, pickDTOS);
            return ResponseEntity.ok(updatedPickDTOS);
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
