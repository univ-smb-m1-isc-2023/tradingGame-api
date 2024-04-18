package tyg.tradinggame.tradinggame.controller.dto.game;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import tyg.tradinggame.tradinggame.controller.dto.stock.StockValueDTOs.StockValueOutDTOForOverview;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderStatusEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

public class StockOrderDTOs {

        public static record StockOrderBasicAttributesInDTO(
                        @NotNull(message = "Order Type is mandatory") OrderTypeEnum type,
                        @NotNull(message = "Price is mandatory") @Min(value = 0, message = "Price must be positive") double price,
                        @NotNull(message = "Quantity is mandatory") @Min(value = 1, message = "Quantity must be positive") Long quantity,
                        LocalDate expirationGameDate,
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
                        double executionPrice,
                        Long quantity,
                        LocalDate creationGameDate,
                        LocalDate expirationGameDate,
                        StockValueOutDTOForOverview stockValue,
                        OrderStatusEnum status) {
        }
}
