package us.gridiron.application.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import us.gridiron.application.models.*;
import us.gridiron.application.payload.response.*;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //**** custom mappings defined here *****/

        // mapping user with only necessary information
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .setConverter(context -> {
                    User source = context.getSource();

                    UserDTO destination = new UserDTO();
                    destination.setId(source.getId());
                    destination.setUsername(source.getUsername());
                    destination.setEmail(source.getEmail());
                    Set<Role> roles = new HashSet<>(source.getRoles());
                    destination.setRoles(roles);

                    return destination;
                });

        // mapping Pick to PickDTO
        modelMapper.createTypeMap(Pick.class, PickDTO.class)
                .setConverter(context -> {
                    Pick source = context.getSource();

                    PickDTO destination = new PickDTO();
                    destination.setId(source.getId());
                    destination.setOwner(modelMapper.map(source.getOwner(), UserDTO.class));
                    destination.setLeague(modelMapper.map(source.getLeague(), LeagueResponseDTO.class));
                    destination.setValue(source.getValue());
                    destination.setWeek(source.getWeek());
                    if(source.getCompetitor() != null) {
                        destination.setCompetitor(modelMapper.map(source.getCompetitor(), CompetitorDTO.class));
                    }

                    return destination;
                });

        // mapping League to LeagueResponseDTO
        modelMapper.createTypeMap(League.class, LeagueResponseDTO.class)
                .setConverter(context -> {
                    League source = context.getSource();

                    LeagueResponseDTO destination = new LeagueResponseDTO();
                    destination.setId(source.getId());
                    destination.setLeagueOwner(source.getLeagueOwner());
                    destination.setUserCount(source.getUserCount());
                    destination.setLeagueName(source.getLeagueName());
                    destination.setIsPrivate(source.getIsPrivate());
                    destination.setMaxUsers(source.getMaxUsers());
                    destination.setInviteCode(source.getInviteCode());

                    return destination;
                });

        // mapping Competitor to CompetitorDTO
        modelMapper.createTypeMap(Competitor.class, CompetitorDTO.class)
                .setConverter(context -> {
                    Competitor source = context.getSource();

                    CompetitorDTO destination = new CompetitorDTO();
                    destination.setId(source.getId());
                    destination.setTeam(modelMapper.map(source.getTeam(), TeamDTO.class));
                    destination.setWinner(source.isWinner());
                    destination.setHomeAway(source.getHomeAway());
                    destination.setStartDate(source.getStartDate());
                    destination.setWeek(source.getWeek());
                    destination.setYear(source.getYear());
                    destination.setEventId(source.getEventId());
                    destination.setCompleted(source.isCompleted());

                    return destination;
                });

        modelMapper.createTypeMap(Team.class, TeamDTO.class)
                .setConverter(context -> {
                    Team source = context.getSource();

                    TeamDTO destination = new TeamDTO();
                    destination.setId(source.getId());
                    destination.setName(source.getName());
                    destination.setAbbreviation(source.getAbbreviation());
                    destination.setColor(source.getColor());
                    destination.setAlternateColor(source.getAlternateColor());
                    destination.setLogo(source.getLogo());

                    return destination;
                });

        return modelMapper;
    }
}
