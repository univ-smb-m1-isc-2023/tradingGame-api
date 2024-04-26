package tyg.tradinggame.tradinggame.application.game.logic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tyg.tradinggame.tradinggame.application.game.logic.stockorder.*;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;

public class GameRunner {

    protected final GameLogicService gameLogicService;
    protected final Game game;
    protected List<StockOrder> stockOrders;
    protected Map<Long, DailyStockData> currentDailyStockDatas;

    public GameRunner(GameLogicService gameLogicService,
            Game game) {
        this.gameLogicService = gameLogicService;
        this.game = game;
    }

    protected boolean tryToPassStockOrder(StockOrder stockOrder) {
        switch (stockOrder.getType()) {
            case OrderTypeEnum.BUY_MARKET:
                return BuyMarketStockOrderLogic.tryToPassStockOrder(
                        currentDailyStockDatas.get(stockOrder.getStockValue().getId()), stockOrder);
            case OrderTypeEnum.SELL_MARKET:
                return SellMarketStockOrderLogic.tryToPassStockOrder(
                        currentDailyStockDatas.get(stockOrder.getStockValue().getId()), stockOrder);
            case OrderTypeEnum.BUY_LIMIT:
                return BuyLimitStockOrderLogic.tryToPassStockOrder(
                        currentDailyStockDatas.get(stockOrder.getStockValue().getId()), stockOrder);
            case OrderTypeEnum.SELL_LIMIT:
                return SellLimitStockOrderLogic.tryToPassStockOrder(
                        currentDailyStockDatas.get(stockOrder.getStockValue().getId()), stockOrder);
            default:
                throw new IllegalArgumentException("Invalid order type");
        }
    }

    protected void updateDailyStockDatas() {
        List<DailyStockData> currentDailyStockDatasList = gameLogicService.getCurrentDailyStockDatas(game);
        this.currentDailyStockDatas = currentDailyStockDatasList.stream()
                .collect(Collectors.toMap(
                        dailyStockData -> dailyStockData.getStockValue().getId(),
                        dailyStockData -> dailyStockData));
    }

    protected void updateStockOrdersForHistorical() {
        this.stockOrders = gameLogicService.getPendingStockOrdersForHistorical(game);
    }

    protected void updateStockOrdersForLive() {
        this.stockOrders = gameLogicService.getPendingStockOrdersForLive(game);
    }

    public Game getGame() {
        return game;
    }

    public List<StockOrder> getStockOrders() {
        return stockOrders;
    }

    public void setStockOrders(List<StockOrder> stockOrders) {
        this.stockOrders = stockOrders;
    }

}
