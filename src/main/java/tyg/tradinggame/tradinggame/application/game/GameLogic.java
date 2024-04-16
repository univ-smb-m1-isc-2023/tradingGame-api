package tyg.tradinggame.tradinggame.application.game;

import java.time.LocalDate;
import java.util.List;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import java.util.concurrent.TimeUnit;

public class GameLogic {

    private final GameComputationService gameComputationService;
    private final Game game;

    public GameLogic(GameComputationService gameComputationService, Game game) {
        this.gameComputationService = gameComputationService;
        this.game = game;
    }

    public void run() {
        System.err.println("Running game with id: " + game.getId());
        List<LocalDate> moveDates = gameComputationService.getMoveDates(game);
        for (LocalDate date : moveDates) {
            System.err.println("Running game with date: " + date);
            try {
                TimeUnit.MILLISECONDS.sleep(game.getMoveDuration().toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
