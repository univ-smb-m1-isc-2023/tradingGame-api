package tyg.tradinggame.tradinggame.controller.dto.game;

import java.util.List;

import tyg.tradinggame.tradinggame.controller.dto.game.StockOrderDTOs.StockOrderOutDTO;

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
                        double availableBalance,
                        double lastMonthProfit,
                        double lastYearProfit,
                        List<StockOrderOutDTO> stockOrders) {
        }

}
