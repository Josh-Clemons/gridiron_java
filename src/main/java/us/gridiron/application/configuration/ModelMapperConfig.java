package us.gridiron.application.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import us.gridiron.application.models.Role;
import us.gridiron.application.models.User;
import us.gridiron.application.payload.response.UserDTO;

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

        return modelMapper;
    }
}
