package tyg.tradinggame.tradinggame.application.game;

import java.util.List;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.WalletRepository;

@Service
public class WalletComputationService {

    private final WalletRepository walletRepository;

    public WalletComputationService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public double availableBalance(Wallet wallet) {
        List<StockOrder> stockOrders = walletRepository.findPendingOrdersOfAWallet(wallet);
        double reservedBalance = 0;
        for (StockOrder stockOrder : stockOrders) {
            reservedBalance += stockOrder.getPrice() * stockOrder.getQuantity();
        }
        return wallet.getBalance() - reservedBalance;
    }

}
