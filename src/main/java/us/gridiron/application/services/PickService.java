package us.gridiron.application.services;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.Competitor;
import us.gridiron.application.models.League;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.request.PickUpdateRequest;
import us.gridiron.application.payload.response.PickDTO;
import us.gridiron.application.repository.CompetitorRepository;
import us.gridiron.application.repository.LeagueRepository;
import us.gridiron.application.repository.PickRepository;
import us.gridiron.application.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PickService {

    private static final Logger logger = LoggerFactory.getLogger(PickService.class);
    private final PickRepository pickRepository;
    private final LeagueRepository leagueRepository;
    private final ModelMapper modelMapper;
    private final CompetitorRepository competitorRepository;
    private final UserRepository userRepository;

    public PickService(PickRepository pickRepository, LeagueRepository leagueRepository, ModelMapper modelMapper,
        CompetitorRepository competitorRepository, UserRepository userRepository) {
        this.pickRepository = pickRepository;
        this.leagueRepository = leagueRepository;
        this.modelMapper = modelMapper;
        this.competitorRepository = competitorRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createPicksForNewUser(User user, League league){
        List<Pick> newPicks = new ArrayList<>();

        for(int i=1; i<=18; i++) {
            newPicks.add(new Pick(user, league, 1, i));
            newPicks.add(new Pick(user, league, 3, i));
            newPicks.add(new Pick(user, league, 5, i));
        }

        pickRepository.saveAll(newPicks);
    }

    @Transactional
    public List<PickDTO> findPicksByUserAndLeagueId(User user, Long leagueId) {

        // I can probably eliminate this repository call by passing in the entire league,
        // keeping it this way for now (mostly to simplify testing)
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(()-> new RuntimeException("Unable to find league"));
        boolean isLeagueMember = league.getUsers().stream().anyMatch(leagueUser -> leagueUser.equals(user));
        if(!isLeagueMember){
            throw new RuntimeException("User not in league");
        }
        List<Pick> picks = pickRepository.findByOwnerAndLeague(user, league);
        return picks.stream().map(pick ->
                modelMapper.map(pick, PickDTO.class)).toList();
    }
    @Transactional
    public List<PickDTO> findLeaguePicks(String inviteCode) {

        return pickRepository.findPicksByLeagueInviteCode(inviteCode)
                .stream().map(pick -> modelMapper.map(pick, PickDTO.class)).toList();

    }

    @Transactional
    public List<PickDTO> updateUserPicks(User user, List<PickDTO> pickDTOS) {
        // converts DTO back to entity
        List<Pick> picks = pickDTOS.stream()
                .map(pickDTO -> modelMapper.map(pickDTO, Pick.class)).toList();

        boolean isOwner = picks.stream().noneMatch(pick -> pick.getOwner().equals(user));
        if(!isOwner){
            throw new RuntimeException("Those are not your picks!");
        }

        List<Pick> updatedPicks = pickRepository.saveAll(picks);
        return updatedPicks.stream()
                .map(pick -> modelMapper.map(pick, PickDTO.class)).toList();
    }

    @Transactional
    public List<Pick> convertPickUpdateRequestToPicksList(List<PickUpdateRequest> pickUpdates) {

        User owner = userRepository.findById(pickUpdates.get(0).getOwnerId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        League league = leagueRepository.findById(pickUpdates.get(0).getLeagueId())
            .orElseThrow(()-> new RuntimeException("League not found"));

        List<Pick> updatedPicks = new ArrayList<>();
        List<Competitor> competitors = competitorRepository.findAll();

        for(PickUpdateRequest pick: pickUpdates) {
            Pick updatedPick = new Pick();
            updatedPick.setId(pick.getId());
            updatedPick.setOwner(owner);
            updatedPick.setLeague(league);
            updatedPick.setWeek(pick.getWeek());
            updatedPick.setValue(pick.getValue());

            Optional<Competitor> matchingCompetitor = competitors.stream()
                .filter(competitor -> competitor.getWeek().equals(pick.getWeek())
                    && competitor.getTeam().getAbbreviation().equals(pick.getTeam()))
                .findFirst();

            if (matchingCompetitor.isPresent()) {
                updatedPick.setCompetitor(matchingCompetitor.get());
            } else {
                updatedPick.setCompetitor(null);
            }
            updatedPicks.add(updatedPick);
        }

        return updatedPicks;
    }

}
