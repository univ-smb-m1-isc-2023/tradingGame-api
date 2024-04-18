package tyg.tradinggame.tradinggame.application.game.logic;

import java.util.Iterator;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;

@Service
public class GameScheduler {
    private final GameLogicService gameLogicService;

    private List<GameRunner> gameRunners;

    public GameScheduler(GameLogicService gameLogicService) {
        this.gameLogicService = gameLogicService;
        this.initializeGameRunners();
    }

    private void initializeGameRunners() {
        this.gameRunners = gameLogicService.getCurrentGames().stream()
                .map(game -> new GameRunner(
                        gameLogicService, game))
                .toList();
    }

    public void addNewGame(Game game) {
        GameRunner gameRunner = new GameRunner(gameLogicService, game);
        gameRunners.add(gameRunner);
    }

    //@Scheduled(fixedDelay = 1000)
    public void run() {
        System.err.println("Running");
        Iterator<GameRunner> iterator = gameRunners.iterator();
        while (iterator.hasNext()) {
            GameRunner gameRunner = iterator.next();
            gameRunner.move();
            if (gameRunner.getGame().getCurrentGameDate().equals(gameRunner.getGame().getFinalGameDate())) {
                iterator.remove();
            }

        }
    }
}
