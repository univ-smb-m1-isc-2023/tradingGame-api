package tyg.tradinggame.tradinggame.application.game;

import java.util.List;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.PlayerRepository;
import tyg.tradinggame.tradinggame.application.exceptions.PublicEntityNotFoundException;
import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerOutDTOForOwner;
import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerOutDTOForSearch;
import tyg.tradinggame.tradinggame.controller.mappers.game.PlayerMapper;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public PlayerService(
            PlayerRepository playerRepository,
            PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    // CRUD

    public List<PlayerOutDTOForSearch> getAll() {
        List<Player> players = playerRepository.findAll();
        return players.stream().map(player -> playerMapper.toOutDTOForSearch(player)).toList();
    }

    public PlayerOutDTOForOwner getById(Long id) {
        Player player = getPlayerById(id);
        return playerMapper.toOutDTOForOwner(player);
    }

    // Utils

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PublicEntityNotFoundException("Player not found with id " + id));
    }

}
