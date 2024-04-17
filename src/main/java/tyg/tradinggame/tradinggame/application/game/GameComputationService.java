package tyg.tradinggame.tradinggame.application.game;

import java.time.Duration;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockDataRepository;

@Service
public class GameComputationService {

    private final DailyStockDataRepository dailyStockDataRepository;

    public GameComputationService(
            DailyStockDataRepository dailyStockDataRepository) {
        this.dailyStockDataRepository = dailyStockDataRepository;
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

}
