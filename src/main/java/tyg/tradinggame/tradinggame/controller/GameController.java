package tyg.tradinggame.tradinggame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tyg.tradinggame.tradinggame.application.game.GameRepositoryService;
import tyg.tradinggame.tradinggame.application.game.GameRepositoryService.GameOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class GameController {
    private final GameRepositoryService gameRepositoryService;

    public GameController(GameRepositoryService gameRepositoryService) {
        this.gameRepositoryService = gameRepositoryService;
    }

    @GetMapping("/game")
    public List<GameOutDTO> allGames() {
        System.err.println("Getting all games");
        return gameRepositoryService.getAll();
    }

    @GetMapping("/game/unfinshed")
    public List<GameOutDTO> allUnfinshedGames() {
        System.err.println("Getting all unfinshed games");
        return gameRepositoryService.getAllUnfinshed();
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<?> gameById(@PathVariable Long id) {
        try {
            System.err.println("Getting game with id: " + id);
            GameOutDTO game = gameRepositoryService.getById(id);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/game")
    public ResponseEntity<?> createGame(@RequestBody GameRepositoryService.GameInDTO gameInDTO) {
        try {
            System.err.println("Creating game with title: " + gameInDTO.title());
            GameOutDTO game = gameRepositoryService.createGame(gameInDTO);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
