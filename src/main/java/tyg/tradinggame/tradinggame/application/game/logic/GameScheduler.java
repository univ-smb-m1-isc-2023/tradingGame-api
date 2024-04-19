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

@Service
public class GameScheduler {
    private final GameLogicService gameLogicService;

    private List<HistoricalGameRunner> gameRunners = new ArrayList<>();

    public GameScheduler(GameLogicService gameLogicService) {
        this.gameLogicService = gameLogicService;
        this.initializeGameRunners();
    }

    @PostConstruct
    private void resetGameRuns() {
        gameLogicService.setHistoricalGameLoadState(false);
    }

    @Transactional
    private void initializeGameRunners() {
        gameRunners.addAll(gameLogicService.getUnfinishedHistoricalGames().stream()
                .map(game -> new HistoricalGameRunner(
                        gameLogicService, game))
                .toList());
    }

    @Transactional
    private void updateGameRunners() {
        List<Game> newGames = gameLogicService.getNewUnfinishedHistoricalGames();
        List<HistoricalGameRunner> newGameRunners = newGames.stream()
                .map(game -> new HistoricalGameRunner(
                        gameLogicService, game))
                .toList();
        if (newGameRunners.isEmpty()) {
            return;
        }
        gameRunners.addAll(newGameRunners);
    }

    public void addNewGame(Game game) {
        HistoricalGameRunner gameRunner = new HistoricalGameRunner(gameLogicService, game);
        gameRunners.add(gameRunner);
    }

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void advanceHistoricalGames() {
        move();
        updateGameRunners();
    }

    @Transactional
    private void move() {
        System.err.println("Moving");
        Iterator<HistoricalGameRunner> iterator = gameRunners.iterator();
        while (iterator.hasNext()) {
            HistoricalGameRunner gameRunner = iterator.next();
            gameRunner.move();
            if (gameRunner.complated()) {
                iterator.remove();
            }
        }
    }
}
