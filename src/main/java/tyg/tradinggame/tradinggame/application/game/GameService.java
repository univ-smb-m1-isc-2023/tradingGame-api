package tyg.tradinggame.tradinggame.application.game;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tyg.tradinggame.tradinggame.application.exceptions.PublicIllegalArgumentException;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.GameDTOs.GameOutDTO;
import tyg.tradinggame.tradinggame.controller.mappers.game.GameMapper;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.GameRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.GameTypeEnum;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final WalletService walletService;
    private final GameMapper gameMapper;

    public GameService(GameRepository gameRepository,
            PlayerService playerService,
            WalletService walletService,
            GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.walletService = walletService;
        this.gameMapper = gameMapper;
    }

    @Transactional
    public GameOutDTO createGame(GameBasicAttributesInDTO gameBasicAttributesInDTO) {
        Player admin = playerService.getPlayerById(gameBasicAttributesInDTO.adminId());
        Game game;
        if (gameBasicAttributesInDTO.playerIds().isEmpty()) {
            throw new PublicIllegalArgumentException("At least one player is required");
        }
        if (gameBasicAttributesInDTO.type() == GameTypeEnum.HISTORICAL) {
            if (gameBasicAttributesInDTO.initialDate() == null || gameBasicAttributesInDTO.finalDate() == null) {
                throw new PublicIllegalArgumentException("Initial and final date are required for historical games");
            }
            if (gameBasicAttributesInDTO.initialDate().isAfter(gameBasicAttributesInDTO.finalDate())) {
                throw new PublicIllegalArgumentException("Initial date must be before final date");
            }
            if (gameBasicAttributesInDTO.moveDuration() == null) {
                throw new PublicIllegalArgumentException("Move duration is required for historical games");
            }
            game = new Game(
                    gameBasicAttributesInDTO.title(),
                    gameBasicAttributesInDTO.type(),
                    gameBasicAttributesInDTO.initialDate(),
                    gameBasicAttributesInDTO.finalDate(),
                    gameBasicAttributesInDTO.initialBalance(),
                    gameBasicAttributesInDTO.moveDuration(),
                    admin);
        } else if (gameBasicAttributesInDTO.type() == GameTypeEnum.LIVE) {
            game = new Game(
                    gameBasicAttributesInDTO.title(),
                    gameBasicAttributesInDTO.type(),
                    LocalDate.now(),
                    null,
                    gameBasicAttributesInDTO.initialBalance(),
                    Duration.ofDays(1),
                    admin);
        } else {
            throw new PublicIllegalArgumentException("Game type not found");
        }
        gameRepository.save(game);

        List<Wallet> wallets = new ArrayList<>();
        for (Long playerId : gameBasicAttributesInDTO.playerIds()) {
            Player player = playerService.getPlayerById(playerId);

            wallets.add(walletService.createWallet(player, game, gameBasicAttributesInDTO.initialBalance()));
        }
        game.setWallets(wallets);

        return gameMapper.toOutDTO(game);
    }

    public List<GameOutDTO> getAll() {
        List<Game> games = gameRepository.findAll();
        List<GameOutDTO> gameOutDTOs = new ArrayList<>();
        for (Game game : games) {
            gameOutDTOs.add(gameMapper.toOutDTO(game));
        }
        return gameOutDTOs;
    }

    public List<GameOutDTO> getAllUnfinshed() {
        List<Game> games = gameRepository.findUnfinshedGames();
        List<GameOutDTO> gameOutDTOs = new ArrayList<>();
        for (Game game : games) {
            gameOutDTOs.add(gameMapper.toOutDTO(game));
        }
        return gameOutDTOs;
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new PublicIllegalArgumentException("Game not found with id " + id));
    }

    public GameOutDTO getById(Long id) {
        Game game = getGameById(id);
        return gameMapper.toOutDTO(game);
    }

}
