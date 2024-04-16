package tyg.tradinggame.tradinggame.application.game;

import java.time.LocalDate;
import java.util.List;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import java.util.concurrent.TimeUnit;

public class GameLogic {

    private final GameService gameService;
    private final Game game;

    public GameLogic(GameService gameService, Game game) {
        this.gameService = gameService;
        this.game = game;
    }

    public void run() {
        System.err.println("Running game with id: " + game.getId());
        List<LocalDate> moveDates = gameService.getMoveDates(game);
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
