package tyg.tradinggame.tradinggame.application.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.application.exceptions.PublicEntityNotFoundException;
import tyg.tradinggame.tradinggame.application.exceptions.PublicIllegalArgumentException;
import tyg.tradinggame.tradinggame.application.stock.DailyStockDataService;
import tyg.tradinggame.tradinggame.application.stock.StockValueService;
import tyg.tradinggame.tradinggame.controller.dto.game.StockOrderDTOs.StockOrderBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.WalletDTOs.WalletOutDTOForOwner;
import tyg.tradinggame.tradinggame.controller.dto.stock.StockValueDTOs.StockValueOutDTOForOwner;
import tyg.tradinggame.tradinggame.controller.mappers.game.StockValueMapper;
import tyg.tradinggame.tradinggame.controller.mappers.game.StockValueMapper.StockValueWithPriceAndQuantity;
import tyg.tradinggame.tradinggame.controller.mappers.game.WalletMapper;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Game;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.WalletRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final StockOrderService stockOrderService;
    private final StockValueService stockValueService;
    private final DailyStockDataService dailyStockDataService;
    private final WalletComputationService walletComputationService;
    private final WalletMapper walletMapper;
    private final StockValueMapper stockValueMapper;

    public WalletService(WalletRepository walletRepository,
            StockOrderService stockOrderService,
            StockValueService stockValueService,
            DailyStockDataService dailyStockDataService,
            WalletComputationService walletComputationService,
            WalletMapper walletMapper,
            StockValueMapper stockValueMapper) {
        this.walletRepository = walletRepository;
        this.stockOrderService = stockOrderService;
        this.stockValueService = stockValueService;
        this.dailyStockDataService = dailyStockDataService;
        this.walletComputationService = walletComputationService;
        this.walletMapper = walletMapper;
        this.stockValueMapper = stockValueMapper;
    }

    protected Wallet createWallet(Player player, Game game, double initialBalance) {
        Wallet wallet = new Wallet(player, game, initialBalance);
        walletRepository.save(wallet);
        return wallet;
    }

    public WalletOutDTOForOwner createStockOrder(StockOrderBasicAttributesInDTO stockOrderInDTO) {
        Wallet wallet = getWalletById(stockOrderInDTO.walletId());
        LocalDate expirationGameDate;
        if (walletComputationService
                .availableBalance(wallet) < (double) (stockOrderInDTO.price() * stockOrderInDTO.quantity())) {
            throw new PublicIllegalArgumentException("Not enough balance to create this order");
        }
        if (stockOrderInDTO.expirationGameDate() == null) {
            expirationGameDate = wallet.getGame().getFinalGameDate();
        } else if (stockOrderInDTO.expirationGameDate().isBefore(wallet.getGame().getCurrentGameDate())) {
            throw new PublicIllegalArgumentException("Expiration date is before current game date");
        } else {
            expirationGameDate = stockOrderInDTO.expirationGameDate();
        }
        StockValue stockValue = stockValueService.getStockValueById(stockOrderInDTO.stockValueId());
        StockOrder stockOrder = new StockOrder(
                stockOrderInDTO.type(),
                stockOrderInDTO.price(),
                stockOrderInDTO.quantity(),
                expirationGameDate,
                wallet,
                stockValue);
        stockOrderService.save(stockOrder);
        wallet.getStockOrders().add(stockOrder);
        wallet = getWalletById(wallet.getId());
        return walletMapper.toOutDTOForOwner(wallet);
    }

    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new PublicEntityNotFoundException("Wallet not found with id " + walletId));
    }

    public WalletOutDTOForOwner getById(Long walletId) {
        return walletMapper.toOutDTOForOwner(getWalletById(walletId));
    }

    public List<StockValueOutDTOForOwner> getAllStockValuesByWalletId(Long walletId) {
        Wallet wallet = getWalletById(walletId);
        Map<StockValue, Long> stockValuesAndQuantities = walletComputationService.getStockValuesWithQuantities(wallet);

        List<StockValueOutDTOForOwner> stockValueOutDTOs = new ArrayList<>();

        for (StockValue stockValue : stockValuesAndQuantities.keySet()) {
            List<DailyStockData> dailyStockDatas = dailyStockDataService.getAllByDateAndStockValue(
                    wallet.getGame().getCurrentGameDate(), stockValue);

            if (dailyStockDatas.isEmpty()) {
                continue;
            }
            StockValueWithPriceAndQuantity stockValueWithPriceAndQuantity = new StockValueWithPriceAndQuantity(
                    stockValue, dailyStockDatas.get(0).getClose(), stockValuesAndQuantities.get(stockValue));

            stockValueOutDTOs.add(stockValueMapper.toOutDTOForOwner(stockValueWithPriceAndQuantity));
        }

        return stockValueOutDTOs;
    }

}
