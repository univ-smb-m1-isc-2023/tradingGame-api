package tyg.tradinggame.tradinggame.JUnite;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.PlayerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import tyg.tradinggame.tradinggame.security.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@EnabledIf(expression = "${deployment.testing.active}", loadContext = true)
@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private PlayerRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testLoadUserByUsername_WithExistingUsername_ShouldReturnUserDetails() {
        // Préparation de la requête avec un nom d'utilisateur existant
        String existingUsername = "existingUser";

        // Simulation du comportement du repository pour findByUsername
        Player existingUser = new Player();
        existingUser.setUsername(existingUsername);
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        // Appel de la méthode à tester
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(existingUsername);

        // Vérification que la méthode renvoie un UserDetails non nul et avec le bon nom
        // d'utilisateur
        assertNotNull(userDetails);
        assertEquals(existingUsername, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_WithNonExistingUsername_ShouldThrowUsernameNotFoundException() {
        // Préparation de la requête avec un nom d'utilisateur non existant
        String nonExistingUsername = "nonExistingUser";

        // Simulation du comportement du repository pour findByUsername
        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // Appel de la méthode à tester et vérification qu'elle lance bien une
        // UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.userDetailsService().loadUserByUsername(nonExistingUsername);
        });
    }
}
