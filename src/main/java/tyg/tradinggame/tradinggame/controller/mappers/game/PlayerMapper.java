package tyg.tradinggame.tradinggame.controller.mappers.game;

import tyg.tradinggame.tradinggame.application.game.GameComputationService;
import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;

public class PlayerMapper {

    public static PlayerOutDTO toOutDTO(Player player, GameComputationService gameComputationService) {
        return new PlayerOutDTO(
                player.getUsername(),
                player.getId(),
                WalletMapper.toOutDTOForOwnerList(player.getWallets()),
                GameMapper.toOutDTOList(player.getCreatedGames(), gameComputationService));
    }
}
