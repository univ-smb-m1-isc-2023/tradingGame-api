package tyg.tradinggame.tradinggame.application.game.logic;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.GameRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrderRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.WalletRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.GameTypeEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockDataRepository;

@Service
public class GameLogicService {

    private final GameRepository gameRepository;
    private final StockOrderRepository stockOrderRepository;
    private final WalletRepository walletRepository;
    private final DailyStockDataRepository dailyStockDataRepository;

    public GameLogicService(GameRepository gameRepository,
            StockOrderRepository stockOrderRepository,
            WalletRepository walletRepository,
            DailyStockDataRepository dailyStockDataRepository) {
        this.gameRepository = gameRepository;
        this.stockOrderRepository = stockOrderRepository;
        this.walletRepository = walletRepository;
        this.dailyStockDataRepository = dailyStockDataRepository;
    }

    public List<Game> getUnfinishedHistoricalGames() {
        return gameRepository.findUnfinshedGamesByType(GameTypeEnum.HISTORICAL);
    }

    public List<Game> getUnfinishedLiveGames() {
        return gameRepository.findGamesByType(GameTypeEnum.LIVE);
    }

    public List<Game> getNewUnfinishedHistoricalGames() {
        return gameRepository.findUnfinshedNotLoadedGamesByType(GameTypeEnum.HISTORICAL);
    }

    public List<Game> getNewUnfinishedLiveGames() {
        return gameRepository.findUnfinshedNotLoadedGamesByType(GameTypeEnum.LIVE);
    }

    public void setHistoricalGameLoadState(boolean isLoaded) {
        gameRepository.setLoadState(GameTypeEnum.HISTORICAL, isLoaded);
    }

    public Duration getTotalDuration(Game game) {
        return game.getMoveDuration()
                .multipliedBy(
                        dailyStockDataRepository.countDistinctDatesBetween(
                                game.getInitialGameDate(),
                                game.getFinalGameDate()));
    }

    public Duration getRemainingDuration(Game game) {
        return game.getMoveDuration()
                .multipliedBy(
                        dailyStockDataRepository.countDistinctDatesBetween(
                                game.getCurrentGameDate(),
                                game.getFinalGameDate()));
    }

    public Long getRemainingMoveNumber(Game game) {
        return dailyStockDataRepository.countDistinctDatesBetween(
                game.getCurrentGameDate(),
                game.getFinalGameDate());
    }

    public List<LocalDate> getTotalMoveDates(Game game) {
        return dailyStockDataRepository.findDistinctDatesBetween(
                game.getInitialGameDate(),
                game.getFinalGameDate());
    }

    public List<LocalDate> getRemainingMoveDates(Game game) {
        return dailyStockDataRepository.findDistinctDatesBetween(
                game.getCurrentGameDate(),
                game.getFinalGameDate());
    }

    public List<DailyStockData> getCurrentDailyStockDatas(Game game) {
        return dailyStockDataRepository.findByDate(game.getCurrentGameDate());
    }

    public List<StockOrder> getPendingStockOrders(Game game) {
        return stockOrderRepository.findPendingOrdersForGame(game);
    }

    public void persistStockOrderUpdate(StockOrder stockOrder) {
        walletRepository.save(stockOrder.getWallet());
        stockOrderRepository.save(stockOrder);
    }

    public void setGameDate(Game game, LocalDate newDate) {
        game.setCurrentGameDate(newDate);
        gameRepository.save(game);
    }

    public void setLoaded(Game game) {
        game.setLoaded(true);
        gameRepository.save(game);
    }

}
