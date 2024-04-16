package tyg.tradinggame.tradinggame.application.game;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.application.stock.DailyStockDataService;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.GameRepository;

@Service
public class GameComputationService {

    private final GameRepository gameRepository;

    private final DailyStockDataService dailyStockDataService;

    public GameComputationService(GameRepository gameRepository,
            DailyStockDataService dailyStockDataService) {
        this.gameRepository = gameRepository;
        this.dailyStockDataService = dailyStockDataService;
    }

    public Duration getTotalDuration(Game game) {
        return game.getMoveDuration()
                .multipliedBy(
                        dailyStockDataService.countDistinctDatesBetween(
                                game.getInitialGameDate(),
                                game.getFinalGameDate()));
    }

    public Duration getRemainingDuration(Game game) {
        return game.getMoveDuration()
                .multipliedBy(
                        dailyStockDataService.countDistinctDatesBetween(
                                game.getCurrentGameDate(),
                                game.getFinalGameDate()));
    }

    public List<LocalDate> getMoveDates(Game game) {
        return dailyStockDataService.findDistinctDatesBetween(
                game.getInitialGameDate(),
                game.getFinalGameDate());
    }
}
