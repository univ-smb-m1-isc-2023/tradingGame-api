package tyg.tradinggame.tradinggame.application.game;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import tyg.tradinggame.tradinggame.application.stock.DailyStockDataRepositoryService;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.GameRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.GameTypeEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;

@Service
public class GameRepositoryService {

    private final GameRepository gameRepository;

    private final PlayerRepositoryService playerRepositoryService;
    private final WalletRepositoryService walletRepositoryService;
    private final DailyStockDataRepositoryService dailyStockDataRepositoryService;

    public GameRepositoryService(GameRepository gameRepository,
            PlayerRepositoryService playerRepositoryService,
            WalletRepositoryService walletRepositoryService,
            DailyStockDataRepositoryService dailyStockDataRepositoryService) {
        this.gameRepository = gameRepository;
        this.playerRepositoryService = playerRepositoryService;
        this.walletRepositoryService = walletRepositoryService;
        this.dailyStockDataRepositoryService = dailyStockDataRepositoryService;
    }

    // @PostConstruct
    // public void runGames() {
    // List<Game> games = gameRepository.findUnfinshedGames();
    // for (Game game : games) {
    // new GameLogic(this, game).run();
    // }
    // }

    // CRUD

    public GameOutDTO createGame(GameInDTO gameInDTO) {
        Player admin = playerRepositoryService.getPlayerById(gameInDTO.adminId());
        Game game = new Game(
                gameInDTO.title,
                gameInDTO.type,
                gameInDTO.initialDate,
                gameInDTO.finalDate,
                gameInDTO.initialBalance,
                gameInDTO.moveDuration,
                admin);
        gameRepository.save(game);

        List<Wallet> wallets = new ArrayList<>();
        for (Long playerId : gameInDTO.playerIds) {
            Player player = playerRepositoryService.getPlayerById(playerId);
            wallets.add(walletRepositoryService.createWallet(player, game, gameInDTO.initialBalance));
        }
        game.setWallets(wallets);

        return toOutDTO(game);
    }

    public List<GameOutDTO> getAll() {
        List<Game> games = gameRepository.findAll();
        List<GameOutDTO> gameOutDTOs = new ArrayList<>();
        for (Game game : games) {
            gameOutDTOs.add(toOutDTO(game));
        }
        return gameOutDTOs;
    }

    public List<GameOutDTO> getAllUnfinshed() {
        List<Game> games = gameRepository.findUnfinshedGames();
        List<GameOutDTO> gameOutDTOs = new ArrayList<>();
        for (Game game : games) {
            gameOutDTOs.add(toOutDTO(game));
        }
        return gameOutDTOs;
    }

    public GameOutDTO getById(Long id) {
        Game game = getGameById(id);
        return toOutDTO(game);
    }

    // Utils

    public Game getGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with id " + id));
    }

    public Duration getTotalDuration(Game game) {
        return game.getMoveDuration()
                .multipliedBy(
                        dailyStockDataRepositoryService.countDistinctDatesBetween(
                                game.getInitialGameDate(),
                                game.getFinalGameDate()));
    }

    public Duration getRemainingDuration(Game game) {
        return game.getMoveDuration()
                .multipliedBy(
                        dailyStockDataRepositoryService.countDistinctDatesBetween(
                                game.getCurrentGameDate(),
                                game.getFinalGameDate()));
    }

    public List<LocalDate> getMoveDates(Game game) {
        return dailyStockDataRepositoryService.findDistinctDatesBetween(
                game.getInitialGameDate(),
                game.getFinalGameDate());
    }

    // DTOs

    public record GameInDTO(
            String title,
            GameTypeEnum type,
            LocalDate initialDate,
            LocalDate finalDate,
            double initialBalance,
            Duration moveDuration,
            Long adminId,
            List<Long> playerIds) {
    }

    public record GameOutDTO(
            Long id,
            String title,
            GameTypeEnum type,
            LocalDate initialDate,
            LocalDate finalDate,
            LocalDate currentDate,
            double initialBalance,
            Duration moveDuration,
            Long adminId,
            List<Wallet> wallets,
            Duration totalDuration,
            Duration remainingDuration) {
    }

    public GameOutDTO toOutDTO(Game game) {
        Duration totalDuration = getTotalDuration(game);
        Duration remainingDuration = getRemainingDuration(game);
        return new GameOutDTO(
                game.getId(),
                game.getTitle(),
                game.getType(),
                game.getInitialGameDate(),
                game.getFinalGameDate(),
                game.getCurrentGameDate(),
                game.getInitialBalance(),
                game.getMoveDuration(),
                game.getAdmin().getId(),
                game.getWallets(),
                totalDuration,
                remainingDuration);
    }
}
