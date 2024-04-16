package tyg.tradinggame.tradinggame.dto.game;

import tyg.tradinggame.tradinggame.dto.game.StockOrderDTOs.StockOrderOutDTO;

import java.util.List;

public class WalletDTOs {

    public static record WalletOutDTOForAll(
            Long ownerId,
            String ownerUsername,
            double balance,
            double lastMonthProfit,
            double lastYearProfit) {
    }

    public static record WalletOutDTOForOwner(
            Long id,
            Long gameId,
            double balance,
            double lastMonthProfit,
            double lastYearProfit,
            List<StockOrderOutDTO> stockOrders) {
    }

}
