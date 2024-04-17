
package tyg.tradinggame.tradinggame.application.game.logic;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;

public class BuyMarketStockOrderLogic {

    public static boolean tryToPassStockOrder(DailyStockData dailyStockData, StockOrder stockOrder) {
        double price = Utils.randomInRange(dailyStockData.getLow(), dailyStockData.getHigh());
        Utils.deduceSumFromWallet(stockOrder, price);
        return true;
    }

}