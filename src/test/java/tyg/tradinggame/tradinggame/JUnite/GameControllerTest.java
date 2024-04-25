package tyg.tradinggame.tradinggame.JUnite;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.springframework.http.ResponseEntity;
import tyg.tradinggame.tradinggame.application.game.GameService;
import tyg.tradinggame.tradinggame.controller.GameController;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameOutDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.WalletDTOs;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.GameTypeEnum;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @Before
    public void setUp() {
        // Mocking behavior of GameService
        List<GameOutDTO> games = Arrays.asList(
                new GameOutDTO(1L, "Game 1", GameTypeEnum.HISTORICAL,
                        LocalDate.now(), LocalDate.now().plusDays(7),
                        LocalDate.now(), 100.0, Duration.ofHours(1),
                        1L, Arrays.asList(new WalletDTOs.WalletOutDTOForAll(1L, "User1", 100.0, 200.0, 10.0, 50.0)),
                        Duration.ofDays(7), Duration.ofDays(7)),
                new GameOutDTO(2L, "Game 2", GameTypeEnum.HISTORICAL,
                        LocalDate.now(), LocalDate.now().plusDays(7),
                        LocalDate.now(), 100.0, Duration.ofHours(1),
                        2L, Arrays.asList(new WalletDTOs.WalletOutDTOForAll(2L, "User2", 150.0, 250.0, 20.0, 60.0)),
                        Duration.ofDays(7), Duration.ofDays(7))
        );
        when(gameService.getAll()).thenReturn(games);
    }

    @Test
    public void testAllGames() {
        // Call the method to be tested
        List<GameOutDTO> result = gameController.allGames();

        // Verify that the method returned the expected result
        assertEquals(2, result.size());
        // You can add more assertions based on your specific requirements
    }
}
