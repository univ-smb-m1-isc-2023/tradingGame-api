package tyg.tradinggame.tradinggame.application.game.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;

public class GameRunner {

    private final GameLogicService gameLogicService;
    private final Game game;
    private List<StockOrder> stockOrders;
    private List<LocalDateTime> moveTimes;
    private Map<Long, DailyStockData> currentDailyStockDatas;

    public GameRunner(GameLogicService gameLogicService,
            Game game) {
        this.gameLogicService = gameLogicService;
        this.game = game;
        this.initializeMoveTimes();
    }

    private void initializeMoveTimes() {
        Long remainingMoves = gameLogicService.getRemainingMoveNumber(game);
        this.moveTimes = new ArrayList<>();
        LocalDateTime initialDateTime = LocalDateTime.now();
        for (long i = 0; i < remainingMoves; i++) {
            this.moveTimes.add(initialDateTime.plus(game.getMoveDuration().multipliedBy(i)));
        }
    }

    public void move() {
        if (this.moveTimes.get(0).isBefore(LocalDateTime.now())) {
            this.updateStockOrders();
            this.updateDailyStockDatas();
            Iterator<StockOrder> iterator = this.stockOrders.iterator();
            while (iterator.hasNext()) {
                StockOrder stockOrder = iterator.next();
                boolean passed = this.tryToPassStockOrder(stockOrder);
                if (passed) {
                    gameLogicService.updateFromOrder(stockOrder);
                    iterator.remove();
                }
            }
            this.moveTimes.remove(0);
            System.err.println("Game date: " + game.getCurrentGameDate());
            gameLogicService.iterateGameDate(game);
            System.err.println("Game date: " + game.getCurrentGameDate());
        }
    }

    public boolean tryToPassStockOrder(StockOrder stockOrder) {
        System.err.println(currentDailyStockDatas);
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

    private void updateStockOrders() {
        this.stockOrders = gameLogicService.getPendingStockOrders(game);
    }

    public void updateDailyStockDatas() {
        List<DailyStockData> currentDailyStockDatasList = gameLogicService.getCurrentDailyStockDatas(game);
        this.currentDailyStockDatas = currentDailyStockDatasList.stream()
                .collect(Collectors.toMap(
                        dailyStockData -> dailyStockData.getStockValue().getId(),
                        dailyStockData -> dailyStockData));
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

    public List<LocalDateTime> getMoveTimes() {
        return moveTimes;
    }

    public void setMoveTimes(List<LocalDateTime> moveTimes) {
        this.moveTimes = moveTimes;
    }

}
