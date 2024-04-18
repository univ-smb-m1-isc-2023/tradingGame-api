package tyg.tradinggame.tradinggame.controller.mappers.game;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import tyg.tradinggame.tradinggame.application.game.GameComputationService;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;

@Component
public class GameMapper {

    private final WalletMapper walletMapper;
    private final GameComputationService gameComputationService;

    public GameMapper(WalletMapper walletMapper,
            GameComputationService gameComputationService) {
        this.walletMapper = walletMapper;
        this.gameComputationService = gameComputationService;
    }

    public GameOutDTO toOutDTO(Game game) {
        return new GameOutDTO(
                game.getId(),
                game.getTitle(),
                game.getType(),
                game.getInitialGameDate(),
                game.getFinalGameDate(),
                game.getCurrentGameDate(),
                game.getInitialBalance(),
                game.getMoveDuration(),
                game.getAdmin().getId(),
                walletMapper.toOutDTOForAllList(game.getWallets()),
                gameComputationService.getTotalDuration(game),
                gameComputationService.getRemainingDuration(game));
    }

    public List<GameOutDTO> toOutDTOList(List<Game> createdGames) {
        return createdGames.stream().map(game -> toOutDTO(game)).collect(Collectors.toList());
    }
}
