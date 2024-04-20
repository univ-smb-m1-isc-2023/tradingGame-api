package tyg.tradinggame.tradinggame.application.game.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.tools.data.alphavantage.DailyStockDataApiClient;

@Service
public class GameScheduler {
    private final GameLogicService gameLogicService;
    private final DailyStockDataApiClient dailyStockDataApiClient;

    private List<HistoricalGameRunner> historicalGameRunners = new ArrayList<>();
    private List<LiveGameRunner> liveGameRunners = new ArrayList<>();

    public GameScheduler(GameLogicService gameLogicService,
            DailyStockDataApiClient dailyStockDataApiClient) {
        this.gameLogicService = gameLogicService;
        this.dailyStockDataApiClient = dailyStockDataApiClient;
        this.initializeGameRunners();
    }

    @PostConstruct
    private void resetGameRuns() {
        gameLogicService.setHistoricalGameLoadState(false);
    }

    @Transactional
    private void initializeGameRunners() {
        historicalGameRunners.addAll(gameLogicService.getUnfinishedHistoricalGames().stream()
                .map(game -> new HistoricalGameRunner(
                        gameLogicService, game))
                .toList());

        liveGameRunners.addAll(gameLogicService.getUnfinishedLiveGames().stream()
                .map(game -> new LiveGameRunner(
                        gameLogicService, game))
                .toList());
    }

    @Transactional
    private void updateHistoricalGameRunners() {
        List<Game> newGames = gameLogicService.getNewUnfinishedHistoricalGames();
        List<HistoricalGameRunner> newGameRunners = newGames.stream()
                .map(game -> new HistoricalGameRunner(
                        gameLogicService, game))
                .toList();
        if (newGameRunners.isEmpty()) {
            return;
        }
        historicalGameRunners.addAll(newGameRunners);
    }

    @Transactional
    private void updateLiveGameRunners() {
        List<Game> newGames = gameLogicService.getNewUnfinishedLiveGames();
        List<LiveGameRunner> newGameRunners = newGames.stream()
                .map(game -> new LiveGameRunner(
                        gameLogicService, game))
                .toList();
        if (newGameRunners.isEmpty()) {
            return;
        }
        liveGameRunners.addAll(newGameRunners);
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void advanceHistoricalGames() {
        Iterator<HistoricalGameRunner> iterator = historicalGameRunners.iterator();
        while (iterator.hasNext()) {
            HistoricalGameRunner gameRunner = iterator.next();
            gameRunner.move();
            if (gameRunner.isFinished()) {
                iterator.remove();
            }
        }
        updateHistoricalGameRunners();
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void advanceLiveGames() {
        System.err.println("Advancing live games");
        dailyStockDataApiClient.update();
        Iterator<LiveGameRunner> iterator = liveGameRunners.iterator();
        while (iterator.hasNext()) {
            LiveGameRunner gameRunner = iterator.next();
            gameRunner.move();
            if (gameRunner.isFinished()) {
                iterator.remove();
            }
        }
        updateLiveGameRunners();
    }

}
