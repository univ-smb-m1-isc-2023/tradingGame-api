package tyg.tradinggame.tradinggame.application.game;

import java.util.List;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.application.exceptions.PublicEntityNotFoundException;
import tyg.tradinggame.tradinggame.application.stock.StockValueService;
import tyg.tradinggame.tradinggame.dto.game.StockOrderDTOs.StockOrderBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.dto.game.StockOrderDTOs.StockOrderInDTO;
import tyg.tradinggame.tradinggame.dto.game.WalletDTOs.WalletOutDTOForAll;
import tyg.tradinggame.tradinggame.dto.game.WalletDTOs.WalletOutDTOForOwner;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.WalletRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;
import tyg.tradinggame.tradinggame.mappers.game.WalletMapper;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final StockOrderService stockOrderService;
    private final StockValueService stockValueService;

    public WalletService(WalletRepository walletRepository,
            StockOrderService stockOrderService,
            StockValueService stockValueService) {
        this.walletRepository = walletRepository;
        this.stockOrderService = stockOrderService;
        this.stockValueService = stockValueService;
    }

    protected Wallet createWallet(Player player, Game game, double initialBalance) {
        Wallet wallet = new Wallet(player, game, initialBalance);
        walletRepository.save(wallet);
        return wallet;
    }

    public StockOrder createStockOrder(StockOrderBasicAttributesInDTO stockOrderInDTO) {
        Wallet wallet = getWalletById(stockOrderInDTO.walletId());
        StockValue stockValue = stockValueService.getStockValueById(stockOrderInDTO.stockValueId());
        StockOrder stockOrder = new StockOrder(
                stockOrderInDTO.type(),
                stockOrderInDTO.price(),
                stockOrderInDTO.quantity(),
                stockOrderInDTO.expirationGameDate(),
                wallet,
                stockValue);
        wallet.getStockOrders().add(stockOrder);
        walletRepository.save(wallet);
        return stockOrder;
    }

    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new PublicEntityNotFoundException("Wallet not found with id " + walletId));
    }

    public WalletOutDTOForOwner getById(Long walletId) {
        return WalletMapper.toOutDTOForOwner(getWalletById(walletId));
    }

}
