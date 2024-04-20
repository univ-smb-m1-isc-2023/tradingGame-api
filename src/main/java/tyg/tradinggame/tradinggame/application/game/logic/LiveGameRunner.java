package tyg.tradinggame.tradinggame.application.game.logic;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;

public class LiveGameRunner extends GameRunner {
    private List<StockOrder> stockOrders;

    public LiveGameRunner(GameLogicService gameLogicService,
            Game game) {
        super(gameLogicService, game);
    }

    public void move() {
        updateStockOrders();
        updateDailyStockDatas();
        Iterator<StockOrder> iterator = stockOrders.iterator();
        while (iterator.hasNext()) {
            StockOrder stockOrder = iterator.next();
            boolean passed = tryToPassStockOrder(stockOrder);
            if (passed) {
                gameLogicService.persistStockOrderUpdate(stockOrder);
                iterator.remove();
            }
        }
        gameLogicService.setGameDate(game, LocalDate.now());
    }

    public boolean isFinished() {
        return false;
    }

}
