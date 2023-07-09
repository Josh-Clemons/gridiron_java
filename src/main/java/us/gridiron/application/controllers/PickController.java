package us.gridiron.application.controllers;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.request.PickUpdateRequest;
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
    private final ModelMapper modelMapper;

    public PickController(PickService pickService, UserService userService, ModelMapper modelMapper) {
        this.pickService = pickService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/my-league-picks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> findMyLeaguePicks(@RequestParam Long leagueId) {
        logger.info("Get /api/pick/my-league-picks, leagueId: {}", leagueId);
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
    public ResponseEntity<Object> findAllLeaguePicks(@RequestParam String inviteCode) {
        logger.info("Get /api/pick/all-league-picks, inviteCode: {}", inviteCode);
        try {
            return ResponseEntity.ok(pickService.findLeaguePicks(inviteCode));
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body("error finding league picks");
        }
    }

    @PostMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> updateLeaguePicks(@RequestBody List<PickUpdateRequest> pickUpdates){
        logger.info("Post /api/pick/update");
        try{
            User user = userService.getLoggedInUser();
            List<Pick> updatedPicks = pickService.convertPickUpdateRequestToPicksList(pickUpdates);
            List<PickDTO> updatedPickDTOs = updatedPicks.stream().map(pick ->
                modelMapper.map(pick, PickDTO.class)).toList();
            return ResponseEntity.ok(pickService.updateUserPicks(user, updatedPickDTOs));
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
