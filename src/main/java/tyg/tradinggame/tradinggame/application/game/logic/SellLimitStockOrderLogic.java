package tyg.tradinggame.tradinggame.application.game.logic;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;

public class SellLimitStockOrderLogic {
    public static boolean tryToPassStockOrder(DailyStockData dailyStockData, StockOrder stockOrder) {
        if (dailyStockData == null) {
            return false;
        }
        double price = stockOrder.getPrice();
        if (price <= dailyStockData.getHigh()) {
            Utils.addSumToWallet(stockOrder, price);
            return true;
        }
        return false;
    }
}
