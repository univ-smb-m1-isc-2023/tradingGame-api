package tyg.tradinggame.tradinggame.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tyg.tradinggame.tradinggame.application.game.GameRepositoryService;
import tyg.tradinggame.tradinggame.application.game.PlayerRepositoryService;
import tyg.tradinggame.tradinggame.application.game.PlayerRepositoryService.PlayerOutDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class PlayerController {

    private final PlayerRepositoryService playerRepositoryService;
    private final GameRepositoryService gameRepositoryService;

    public PlayerController(PlayerRepositoryService playerRepositoryService,
            GameRepositoryService gameRepositoryService) {
        this.playerRepositoryService = playerRepositoryService;
        this.gameRepositoryService = gameRepositoryService;
    }

    @GetMapping("/player")
    public List<PlayerOutDTO> allPlayers() {
        System.err.println("Getting all players");
        return playerRepositoryService.getAll();
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<?> playerById(@PathVariable Long id) {
        try {
            System.err.println("Getting player with id: " + id);
            PlayerOutDTO player = playerRepositoryService.getById(id);
            return ResponseEntity.ok(player);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/player")
    public ResponseEntity<PlayerOutDTO> createPlayer(@RequestBody PlayerRepositoryService.PlayerInDTO playerInDTO) {
        System.err.println("Creating player with username: " + playerInDTO.username());
        PlayerOutDTO player = playerRepositoryService.createPlayer(playerInDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }

    @PostMapping("/player/{playerId}/game")
    public ResponseEntity<?> createGame(@PathVariable Long playerId,
            @RequestBody GameRepositoryService.GameInDTO gameInDTO) {
        try {
            if (playerId != gameInDTO.adminId())
                return ResponseEntity.badRequest().body("Player id does not match admin id");
            System.err.println("Creating game with title: " + gameInDTO.title());
            GameRepositoryService.GameOutDTO game = gameRepositoryService.createGame(gameInDTO);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
