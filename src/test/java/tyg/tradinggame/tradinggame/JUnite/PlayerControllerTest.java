package tyg.tradinggame.tradinggame.JUnite;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import tyg.tradinggame.tradinggame.application.game.GameService;
import tyg.tradinggame.tradinggame.application.game.PlayerService;
import tyg.tradinggame.tradinggame.controller.PlayerController;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameOutDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.PlayerDTOs.PlayerOutDTOForOwner;
import tyg.tradinggame.tradinggame.controller.dto.game.WalletDTOs;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.GameTypeEnum;
import tyg.tradinggame.tradinggame.security.service.AuthenticationService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@EnabledIf(expression = "${deployment.testing.active}", loadContext = true)
public class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private GameService gameService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private PlayerController playerController;

    @Before
    public void setUp() {

        when(gameService.createGame(any(GameBasicAttributesInDTO.class)))
                .thenReturn(new GameOutDTO(1L, "Game 1", GameTypeEnum.HISTORICAL,
                        LocalDate.now(), LocalDate.now().plusDays(7),
                        LocalDate.now(), 100.0, Duration.ofHours(1),
                        1L, Arrays.asList(new WalletDTOs.WalletOutDTOForAll(1L, "User1", 100.0, 200.0, 10.0, 50.0)),
                        Duration.ofDays(7), Duration.ofDays(7)));

    }

    @Test
    public void testCreateGame() {
        // Prepare input for the testx
        Long playerId = 1L;
        GameBasicAttributesInDTO gameBasicAttributes = new GameBasicAttributesInDTO(
                "Test Game", // Titre
                GameTypeEnum.HISTORICAL, // Type
                LocalDate.now(), // Date de début
                LocalDate.now().plusDays(7), // Date de fin
                100.0, // Solde initial
                Duration.ofHours(1), // Durée de déplacement
                playerId, // ID de l'administrateur
                Collections.singletonList(playerId) // Liste des IDs des joueurs
        );

        // Call the method to be tested
        ResponseEntity<?> responseEntity = playerController.createGame(playerId, gameBasicAttributes);

        // Verify that the method returned the expected result
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        // You can add more assertions based on your specific requirements
    }

}
