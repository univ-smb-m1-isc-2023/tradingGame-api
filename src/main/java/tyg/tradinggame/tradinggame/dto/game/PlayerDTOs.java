package tyg.tradinggame.tradinggame.dto.game;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import tyg.tradinggame.tradinggame.dto.game.WalletDTOs.WalletOutDTOForOwner;
import tyg.tradinggame.tradinggame.dto.game.GameDTOs.GameOutDTO;

public class PlayerDTOs {

    public static record PlayerInDTO(
            @NotBlank(message = "Username is mandatory") String username,
            @NotBlank(message = "Password is mandatory") String password) {
    }

    public static record PlayerOutDTO(
            String username,
            Long id,
            List<WalletOutDTOForOwner> wallets,
            List<GameOutDTO> createdGames) {
    }

}
