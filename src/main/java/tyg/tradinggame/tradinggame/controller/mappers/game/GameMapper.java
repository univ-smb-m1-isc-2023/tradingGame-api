package tyg.tradinggame.tradinggame.controller.mappers.game;

import java.util.List;
import java.util.stream.Collectors;

import tyg.tradinggame.tradinggame.application.game.GameComputationService;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;

public class GameMapper {

    public static GameOutDTO toOutDTO(Game game, GameComputationService gameComputationService) {
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
                WalletMapper.toOutDTOForAllList(game.getWallets()),
                gameComputationService.getTotalDuration(game),
                gameComputationService.getRemainingDuration(game));
    }

    public static List<GameOutDTO> toOutDTOList(List<Game> createdGames,
            GameComputationService gameComputationService) {
        return createdGames.stream().map(game -> toOutDTO(game, gameComputationService)).collect(Collectors.toList());
    }
}
