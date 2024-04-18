package tyg.tradinggame.tradinggame.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.UserGame;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.PlayerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PlayerRepository playerRepository;

    @Autowired
    public CustomUserDetailsService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Player> userOptional = playerRepository.findByUsername(username);
        Player user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(user);
    }
}
