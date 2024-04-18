package tyg.tradinggame.tradinggame.controller.dto.game;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameOutDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.WalletDTOs.WalletOutDTOForOwner;

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
