package tyg.tradinggame.tradinggame.dto.game;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import tyg.tradinggame.tradinggame.dto.stock.StockValueDTOs.StockValueOutDTOForOverview;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

public class StockOrderDTOs {

    public static record StockOrderBasicAttributesInDTO(
            @NotNull(message = "Order Type is mandatory") OrderTypeEnum type,
            @NotNull(message = "Price is mandatory") double price,
            @NotNull(message = "Quantity is mandatory") Long quantity,
            @NotNull(message = "The order expiration date is mandatory") LocalDate expirationGameDate,
            @NotNull(message = "The wallet id is mandatory") Long walletId,
            @NotNull(message = "The stock value id is mandatory") Long stockValueId) {
    }

    public static record StockOrderInDTO(
            OrderTypeEnum type,
            double price,
            Long quantity,
            LocalDate expirationGameDate,
            Wallet wallet,
            StockValue stockValue) {
    }

    public static record StockOrderOutDTO(
            OrderTypeEnum type,
            double price,
            Long quantity,
            LocalDate creationGameDate,
            LocalDate expirationGameDate,
            StockValueOutDTOForOverview stockValue) {
    }
}
