package tyg.tradinggame.tradinggame.JUnite;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.PlayerRepository;
import tyg.tradinggame.tradinggame.security.dao.request.SignUpRequest;
import tyg.tradinggame.tradinggame.security.dao.response.JwtAuthenticationResponse;
import tyg.tradinggame.tradinggame.security.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import tyg.tradinggame.tradinggame.security.service.impl.AuthenticationServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationServiceImplTest {

    @Mock
    private PlayerRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void testSignup_WithValidRequest_ShouldReturnJwtAuthenticationResponse() {
        // Préparation de la demande d'inscription avec des informations valides
        SignUpRequest signUpRequest = new SignUpRequest("username", "password");

        // Simulation du comportement du repository pour findByUsername
        when(userRepository.findByUsername(signUpRequest.getUserName())).thenReturn(Optional.empty());

        // Simulation du comportement du passwordEncoder pour encode
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        // Simulation du comportement du jwtService pour generateToken
        when(jwtService.generateToken(any(Player.class))).thenReturn("jwtToken");

        // Appel de la méthode à tester
        JwtAuthenticationResponse response = authenticationService.signup(signUpRequest);

        // Vérification que la méthode renvoie une réponse avec un token JWT non nul
        assertNotNull(response.getToken());
    }

    // Ajoutez d'autres tests pour tester les autres scénarios, comme la connexion avec des identifiants valides et invalides
}
