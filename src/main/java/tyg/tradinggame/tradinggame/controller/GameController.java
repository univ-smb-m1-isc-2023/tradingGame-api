package tyg.tradinggame.tradinggame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tyg.tradinggame.tradinggame.application.game.GameService;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameOutDTO;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "*")
@RestController
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game")
    public List<GameOutDTO> allGames() {
        System.err.println("Getting all games");
        return gameService.getAll();
    }

    @GetMapping("/game/unfinshed")
    public List<GameOutDTO> allUnfinshedGames() {
        System.err.println("Getting all unfinshed games");
        return gameService.getAllUnfinshed();
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<?> gameById(@PathVariable Long id) {
        try {
            System.err.println("Getting game with id: " + id);
            GameOutDTO game = gameService.getById(id);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/game")
    public ResponseEntity<?> createGame(@RequestBody GameBasicAttributesInDTO gameBasicAttributesInDTO) {
        try {
            System.err.println("Creating game with title: " + gameBasicAttributesInDTO.title());
            GameOutDTO game = gameService.createGame(gameBasicAttributesInDTO);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
