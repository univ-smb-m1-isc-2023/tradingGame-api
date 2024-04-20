package tyg.tradinggame.tradinggame.controller.mappers.game;

import org.springframework.stereotype.Component;

import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerOutDTOForOwner;
import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerOutDTOForSearch;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;

@Component
public class PlayerMapper {

    private final WalletMapper walletMapper;
    private final GameMapper gameMapper;

    public PlayerMapper(WalletMapper walletMapper,
            GameMapper gameMapper) {
        this.walletMapper = walletMapper;
        this.gameMapper = gameMapper;
    }

    public PlayerOutDTOForOwner toOutDTOForOwner(Player player) {
        return new PlayerOutDTOForOwner(
                player.getUsername(),
                player.getId(),
                walletMapper.toOutDTOForOwnerList(player.getWallets()),
                gameMapper.toOutDTOList(player.getCreatedGames()));
    }

    public PlayerOutDTOForSearch toOutDTOForSearch(Player player) {
        return new PlayerOutDTOForSearch(
                player.getUsername(),
                player.getId());
    }
}
