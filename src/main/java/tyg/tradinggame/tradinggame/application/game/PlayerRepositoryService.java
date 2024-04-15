package tyg.tradinggame.tradinggame.application.game;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.PlayerRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;

@Service
public class PlayerRepositoryService {

    private final PlayerRepository playerRepository;

    public PlayerRepositoryService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // CRUD

    public PlayerOutDTO createPlayer(PlayerInDTO playerInDTO) {
        Player player = new Player(playerInDTO.username(), playerInDTO.password());
        playerRepository.save(player);

        return toOutDTO(player);
    }

    public List<PlayerOutDTO> getAll() {
        List<Player> players = playerRepository.findAll();
        return players.stream().map(this::toOutDTO).toList();
    }

    public PlayerOutDTO getById(Long id) {
        Player player = getPlayerById(id);
        return toOutDTO(player);
    }

    // Utils

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id " + id));
    }

    // DTOs

    public record PlayerInDTO(String username, String password) {
    }

    public record PlayerOutDTO(String username, Long id, List<Wallet> wallets, List<Game> createdGames) {
    }

    public PlayerOutDTO toOutDTO(Player player) {
        return new PlayerOutDTO(player.getUsername(), player.getId(), player.getWallets(), player.getCreatedGames());
    }
}
