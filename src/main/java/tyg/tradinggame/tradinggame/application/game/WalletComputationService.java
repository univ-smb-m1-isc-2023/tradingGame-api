package tyg.tradinggame.tradinggame.application.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.controller.dto.stock.StockValueDTOs.StockValueOutDTOForGame;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.WalletRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockDataRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValueRepository;

@Service
public class WalletComputationService {

    private final WalletRepository walletRepository;
    private final DailyStockDataRepository dailyStockDataRepository;
    private final StockValueRepository stockValueRepository;

    public WalletComputationService(WalletRepository walletRepository,
            DailyStockDataRepository dailyStockDataRepository,
            StockValueRepository stockValueRepository) {
        this.walletRepository = walletRepository;
        this.dailyStockDataRepository = dailyStockDataRepository;
        this.stockValueRepository = stockValueRepository;
    }

    public double availableBalance(Wallet wallet) {
        List<StockOrder> stockOrders = walletRepository.findPendingOrdersOfAWallet(wallet);
        double reservedBalance = 0;
        for (StockOrder stockOrder : stockOrders) {
            reservedBalance += stockOrder.getPrice() * stockOrder.getQuantity();
        }
        return wallet.getBalance() - reservedBalance;
    }

    public double totalAssets(Wallet wallet) {
        List<StockOrder> stockOrders = walletRepository.findExecutedOrdersOfAWallet(wallet);
        double assetResult = wallet.getGame().getInitialBalance();
        for (StockOrder stockOrder : stockOrders) {
            if (stockOrder.getType().equals(OrderTypeEnum.BUY_LIMIT)
                    || stockOrder.getType().equals(OrderTypeEnum.BUY_MARKET)) {
                assetResult -= stockOrder.getExecutionPrice() * stockOrder.getQuantity();
            } else if (stockOrder.getType().equals(OrderTypeEnum.SELL_LIMIT)
                    || stockOrder.getType().equals(OrderTypeEnum.SELL_MARKET)) {
                assetResult += stockOrder.getExecutionPrice() * stockOrder.getQuantity();
            }
        }
        List<OrderTypeEnum> buyTypes = Arrays.asList(OrderTypeEnum.BUY_LIMIT, OrderTypeEnum.BUY_MARKET);
        List<OrderTypeEnum> sellTypes = Arrays.asList(OrderTypeEnum.SELL_LIMIT, OrderTypeEnum.SELL_MARKET);

        List<StockValue> stockValues = stockValueRepository.findAll();

        double stockAssetSum = 0;

        for (StockValue stockValue : stockValues) {
            Long boughtStocks = walletRepository.countBoughtStocksOfAValue(wallet, stockValue);
            Long soldStocks = walletRepository.countSoldStocksOfAValue(wallet, stockValue);

            long boughtStocksSum = boughtStocks != null ? boughtStocks.longValue() : 0;
            long soldStocksSum = soldStocks != null ? soldStocks.longValue() : 0;

            List<DailyStockData> dailyStockDatas = dailyStockDataRepository
                    .findByDateAndStockValue(wallet.getGame().getCurrentGameDate(), stockValue);
            if (dailyStockDatas.isEmpty()) {
                continue;
            }
            double stockValuePrice = dailyStockDatas.get(0).getClose();
            stockAssetSum += (boughtStocksSum - soldStocksSum) * stockValuePrice;
        }

        return assetResult + stockAssetSum;

    }

    public Map<StockValue, Long> getStockValuesWithQuantities(Wallet wallet) {
        Map<StockValue, Long> stockValueQuantities = new HashMap<>();

        List<StockValue> stockValues = stockValueRepository.findAll();

        for (StockValue stockValue : stockValues) {
            Long boughtStocks = walletRepository.countBoughtStocksOfAValue(wallet, stockValue);
            Long soldStocks = walletRepository.countSoldStocksOfAValue(wallet, stockValue);

            long boughtStocksSum = boughtStocks != null ? boughtStocks.longValue() : 0;
            long soldStocksSum = soldStocks != null ? soldStocks.longValue() : 0;

            long quantity = boughtStocksSum - soldStocksSum;

            stockValueQuantities.put(stockValue, quantity);
        }
        return stockValueQuantities;
    }
}
