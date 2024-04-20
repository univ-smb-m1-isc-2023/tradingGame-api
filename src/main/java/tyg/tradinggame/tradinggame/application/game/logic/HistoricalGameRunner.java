package tyg.tradinggame.tradinggame.application.game.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;

public class HistoricalGameRunner extends GameRunner {

    private List<LocalDateTime> moveScheduleTimes;
    private List<LocalDate> moveDates;

    public HistoricalGameRunner(GameLogicService gameLogicService,
            Game game) {
        super(gameLogicService, game);
        this.initializeMoveTimes();
        this.moveDates = new ArrayList<>(gameLogicService.getTotalMoveDates(game));
    }

    @Transactional
    private void initializeMoveTimes() {
        Long remainingMoves = gameLogicService.getRemainingMoveNumber(game);
        moveScheduleTimes = new ArrayList<>();
        LocalDateTime initialDateTime = LocalDateTime.now();
        for (long i = 0; i < remainingMoves; i++) {
            moveScheduleTimes.add(initialDateTime.plus(game.getMoveDuration().multipliedBy(i)));
        }
        gameLogicService.setLoaded(game);

    }

    public void move() {
        if (moveScheduleTimes.isEmpty()) {
            return;
        }
        if (moveScheduleTimes.get(0).isBefore(LocalDateTime.now())) {
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
            moveScheduleTimes.remove(0);
            moveDates.remove(0);
            System.err.println("Game date: " + game.getCurrentGameDate());
            gameLogicService.setGameDate(game, moveDates.get(0));
            System.err.println("Game date: " + game.getCurrentGameDate());
        }
    }

    public List<LocalDateTime> getMoveScheduleTimes() {
        return moveScheduleTimes;
    }

    public void setMoveScheduleTimes(List<LocalDateTime> moveTimes) {
        this.moveScheduleTimes = moveTimes;
    }

    public boolean isFinished() {
        return moveScheduleTimes.isEmpty();
    }

}
