package tyg.tradinggame.tradinggame.controller.mappers.game;

import org.springframework.stereotype.Component;

import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerOutDTO;
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

    public PlayerOutDTO toOutDTO(Player player) {
        return new PlayerOutDTO(
                player.getUsername(),
                player.getId(),
                walletMapper.toOutDTOForOwnerList(player.getWallets()),
                gameMapper.toOutDTOList(player.getCreatedGames()));
    }
}
