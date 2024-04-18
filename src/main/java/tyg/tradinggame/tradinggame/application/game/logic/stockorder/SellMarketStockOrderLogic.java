package tyg.tradinggame.tradinggame.application.game.logic.stockorder;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;

public class SellMarketStockOrderLogic {

    public static boolean tryToPassStockOrder(DailyStockData dailyStockData, StockOrder stockOrder) {
        if (dailyStockData == null) {
            return false;
        }
        double price = Utils.randomInRange(dailyStockData.getLow(), dailyStockData.getHigh());
        Utils.executeSell(stockOrder, price);
        return true;
    }
}
