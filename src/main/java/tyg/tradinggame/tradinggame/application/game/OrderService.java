package tyg.tradinggame.tradinggame.application.game;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum;

@Service
public class OrderService {

    public record StockOrderDTO(
            Long playerId,
            Long walletId,
            Long stockId,
            Long quantity,
            double price,
            OrderTypeEnum orderType) {
    }
}
