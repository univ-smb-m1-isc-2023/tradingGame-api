package tyg.tradinggame.tradinggame.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tyg.tradinggame.tradinggame.application.game.GameService;
import tyg.tradinggame.tradinggame.application.game.PlayerService;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameOutDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerInDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerOutDTOForOwner;
import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerOutDTOForSearch;
import tyg.tradinggame.tradinggame.security.service.AuthenticationService;

@CrossOrigin(origins = "*")
@RestController
public class PlayerController {

    private final PlayerService playerService;
    private final GameService gameService;

    private final AuthenticationService authenticationService;

    public PlayerController(PlayerService playerService,
            GameService gameService, AuthenticationService authenticationService) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/player")
    public List<PlayerOutDTOForSearch> allPlayers() {
        System.err.println("Getting all players");
        return playerService.getAll();
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<?> playerById(@PathVariable Long id) {
        try {
            System.err.println("Getting player with id: " + id);
            PlayerOutDTOForOwner player = playerService.getById(id);
            return ResponseEntity.ok(player);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

    @DeleteMapping("player/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        authenticationService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

}
