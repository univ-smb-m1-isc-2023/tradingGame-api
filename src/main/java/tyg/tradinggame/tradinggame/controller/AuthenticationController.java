package tyg.tradinggame.tradinggame.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tyg.tradinggame.tradinggame.security.jwt.JwtTokenProvider;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.PlayerRepository;
import tyg.tradinggame.tradinggame.security.CustomUserDetails;
import tyg.tradinggame.tradinggame.security.CustomUserDetailsService;
import tyg.tradinggame.tradinggame.security.dto.AuthenticationRequest;
import tyg.tradinggame.tradinggame.security.dto.AuthenticationResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final PlayerRepository playerRepository;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                                    CustomUserDetailsService customUserDetailsService, PlayerRepository playerRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.playerRepository = playerRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            String token = jwtTokenProvider.generateToken(userDetails);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthenticationRequest registrationRequest) {
        if (playerRepository.existsByUsername(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        Player player = new Player();
        player.setUsername(registrationRequest.getUsername());
        player.setPassword(registrationRequest.getPassword());

        playerRepository.save(player);
        return ResponseEntity.ok("User registered successfully");
    }
}
