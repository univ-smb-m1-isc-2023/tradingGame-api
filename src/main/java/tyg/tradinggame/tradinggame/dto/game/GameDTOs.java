package tyg.tradinggame.tradinggame.dto.game;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tyg.tradinggame.tradinggame.dto.game.WalletDTOs.WalletOutDTOForAll;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.GameTypeEnum;

public class GameDTOs {

    public record GameBasicAttributesInDTO(
            @NotBlank(message = "Title is mandatory") String title,
            @NotBlank(message = "Type is mandatory") GameTypeEnum type,
            @NotNull(message = "Initial date is mandatory") LocalDate initialDate,
            @NotNull(message = "Final date is mandatory") LocalDate finalDate,
            @NotNull(message = "Initial balance is mandatory") double initialBalance,
            @NotNull(message = "Move duration is mandatory") Duration moveDuration,
            @NotNull(message = "Admin id is mandatory") Long adminId,
            @NotEmpty(message = "Player ids are mandatory") List<Long> playerIds) {
    }

    public record GameOutDTO(
            Long id,
            String title,
            GameTypeEnum type,
            LocalDate initialDate,
            LocalDate finalDate,
            LocalDate currentDate,
            double initialBalance,
            Duration moveDuration,
            Long adminId,
            List<WalletOutDTOForAll> wallets,
            Duration totalDuration,
            Duration remainingDuration) {

    }

}
