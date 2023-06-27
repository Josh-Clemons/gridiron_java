package us.gridiron.application.services;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import us.gridiron.application.models.League;
import us.gridiron.application.models.Pick;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.response.PickDTO;
import us.gridiron.application.repository.LeagueRepository;
import us.gridiron.application.repository.PickRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PickService {


    private final PickRepository pickRepository;
    private final LeagueRepository leagueRepository;
    private final ModelMapper modelMapper;

    public PickService(PickRepository pickRepository, LeagueRepository leagueRepository, ModelMapper modelMapper) {
        this.pickRepository = pickRepository;
        this.leagueRepository = leagueRepository;
        this.modelMapper = modelMapper;
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

        List<Pick> picks = pickRepository.findByOwnerAndLeague(user, league);
        return picks.stream().map(pick ->
                modelMapper.map(pick, PickDTO.class)).toList();
    }

}
