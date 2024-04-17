package tyg.tradinggame.tradinggame.application.game.logic;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;

public class BuyLimitStockOrderLogic {

    public static boolean tryToPassStockOrder(DailyStockData dailyStockData, StockOrder stockOrder) {
        if (dailyStockData == null) {
            return false;
        }
        double price = stockOrder.getPrice();
        if (price >= dailyStockData.getLow()) {
            Utils.deduceSumFromWallet(stockOrder, price);
            return true;
        }
        return false;
    }

}