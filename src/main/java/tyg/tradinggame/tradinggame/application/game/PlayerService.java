package tyg.tradinggame.tradinggame.application.game;

import java.util.List;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.PlayerRepository;
import tyg.tradinggame.tradinggame.mappers.game.PlayerMapper;
import tyg.tradinggame.tradinggame.application.exceptions.PublicEntityNotFoundException;
import tyg.tradinggame.tradinggame.dto.game.PlayerDTOs.PlayerInDTO;
import tyg.tradinggame.tradinggame.dto.game.PlayerDTOs.PlayerOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final GameComputationService gameComputationService;

    public PlayerService(
            PlayerRepository playerRepository,
            GameComputationService gameComputationService) {
        this.playerRepository = playerRepository;
        this.gameComputationService = gameComputationService;
    }

    // CRUD

//    public PlayerOutDTO createPlayer(PlayerInDTO playerInDTO) {
//        Player player = new Player(playerInDTO.username(), playerInDTO.password());
//        playerRepository.save(player);

//        return PlayerMapper.toOutDTO(player, gameComputationService);
//    }

    public List<PlayerOutDTO> getAll() {
        List<Player> players = playerRepository.findAll();
        return players.stream().map(game -> PlayerMapper.toOutDTO(game, gameComputationService)).toList();
    }

    public PlayerOutDTO getById(Long id) {
        Player player = getPlayerById(id);
        return PlayerMapper.toOutDTO(player, gameComputationService);
    }

    // Utils

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PublicEntityNotFoundException("Player not found with id " + id));
    }

}
