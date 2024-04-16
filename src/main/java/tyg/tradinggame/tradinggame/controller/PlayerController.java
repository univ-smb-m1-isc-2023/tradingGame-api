package tyg.tradinggame.tradinggame.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tyg.tradinggame.tradinggame.application.game.GameService;
import tyg.tradinggame.tradinggame.application.game.PlayerService;
import tyg.tradinggame.tradinggame.dto.game.GameDTOs.GameBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.dto.game.GameDTOs.GameOutDTO;
import tyg.tradinggame.tradinggame.dto.game.PlayerDTOs.PlayerInDTO;
import tyg.tradinggame.tradinggame.dto.game.PlayerDTOs.PlayerOutDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class PlayerController {

    private final PlayerService playerService;
    private final GameService gameService;

    public PlayerController(PlayerService playerService,
            GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @GetMapping("/player")
    public List<PlayerOutDTO> allPlayers() {
        System.err.println("Getting all players");
        return playerService.getAll();
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<?> playerById(@PathVariable Long id) {
        try {
            System.err.println("Getting player with id: " + id);
            PlayerOutDTO player = playerService.getById(id);
            return ResponseEntity.ok(player);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/player")
    public ResponseEntity<PlayerOutDTO> createPlayer(@RequestBody PlayerInDTO playerInDTO) {
        System.err.println("Creating player with username: " + playerInDTO.username());
        PlayerOutDTO player = playerService.createPlayer(playerInDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }

    @PostMapping("/player/{playerId}/game")
    public ResponseEntity<?> createGame(@PathVariable Long playerId,
            @RequestBody GameBasicAttributesInDTO gameBasicAttributesInDTO) {
        try {
            if (playerId != gameBasicAttributesInDTO.adminId())
                return ResponseEntity.badRequest().body("Player id does not match admin id");
            System.err.println("Creating game with title: " + gameBasicAttributesInDTO.title());
            GameOutDTO game = gameService.createGame(gameBasicAttributesInDTO);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
