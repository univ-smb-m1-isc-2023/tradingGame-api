package tyg.tradinggame.tradinggame.application.game;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.application.exceptions.PublicIllegalArgumentException;
import tyg.tradinggame.tradinggame.dto.game.GameDTOs.GameBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.dto.game.GameDTOs.GameOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.GameRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.mappers.game.GameMapper;

@Service
public class GameService {

    private final GameRepository gameRepository;

    private final PlayerService playerService;
    private final WalletService walletService;
    private final GameComputationService gameComputationService;

    public GameService(GameRepository gameRepository,
            PlayerService playerService,
            WalletService walletService,
            GameComputationService gameComputationService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.walletService = walletService;
        this.gameComputationService = gameComputationService;
    }

    // CRUD

    public GameOutDTO createGame(GameBasicAttributesInDTO gameBasicAttributesInDTO) {
        Player admin = playerService.getPlayerById(gameBasicAttributesInDTO.adminId());
        Game game = new Game(
                gameBasicAttributesInDTO.title(),
                gameBasicAttributesInDTO.type(),
                gameBasicAttributesInDTO.initialDate(),
                gameBasicAttributesInDTO.finalDate(),
                gameBasicAttributesInDTO.initialBalance(),
                gameBasicAttributesInDTO.moveDuration(),
                admin);
        gameRepository.save(game);

        List<Wallet> wallets = new ArrayList<>();
        for (Long playerId : gameBasicAttributesInDTO.playerIds()) {
            Player player = playerService.getPlayerById(playerId);
            wallets.add(walletService.createWallet(player, game, gameBasicAttributesInDTO.initialBalance()));
        }
        game.setWallets(wallets);

        return GameMapper.toOutDTO(game, gameComputationService);
    }

    public List<GameOutDTO> getAll() {
        List<Game> games = gameRepository.findAll();
        List<GameOutDTO> gameOutDTOs = new ArrayList<>();
        for (Game game : games) {
            gameOutDTOs.add(GameMapper.toOutDTO(game, gameComputationService));
        }
        return gameOutDTOs;
    }

    public List<GameOutDTO> getAllUnfinshed() {
        List<Game> games = gameRepository.findUnfinshedGames();
        List<GameOutDTO> gameOutDTOs = new ArrayList<>();
        for (Game game : games) {
            gameOutDTOs.add(GameMapper.toOutDTO(game, gameComputationService));
        }
        return gameOutDTOs;
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new PublicIllegalArgumentException("Game not found with id " + id));
    }

    public GameOutDTO getById(Long id) {
        Game game = getGameById(id);
        return GameMapper.toOutDTO(game, gameComputationService);
    }

}
