package tyg.tradinggame.tradinggame.controller.mappers.game;

import java.util.List;

import org.springframework.stereotype.Component;

import tyg.tradinggame.tradinggame.controller.dto.game.WalletDTOs.WalletOutDTOForAll;
import tyg.tradinggame.tradinggame.controller.dto.game.WalletDTOs.WalletOutDTOForOwner;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;

@Component
public class WalletMapper {

    private final StockOrderMapper stockOrderMapper;

    public WalletMapper(StockOrderMapper stockOrderMapper) {
        this.stockOrderMapper = stockOrderMapper;
    }

    public WalletOutDTOForOwner toOutDTOForOwner(Wallet wallet) {
        return new WalletOutDTOForOwner(
                wallet.getId(),
                wallet.getGame().getId(),
                wallet.getBalance(),
                wallet.getLastMonthProfit(),
                wallet.getLastYearProfit(),
                stockOrderMapper.toOutDTOList(wallet.getStockOrders()));
    }

    public List<WalletOutDTOForOwner> toOutDTOForOwnerList(List<Wallet> wallets) {
        return wallets.stream().map(this::toOutDTOForOwner).toList();
    }

    public WalletOutDTOForAll toOutDTOForAll(Wallet wallet) {
        return new WalletOutDTOForAll(
                wallet.getOwner().getId(),
                wallet.getOwner().getUsername(),
                wallet.getBalance(),
                wallet.getLastMonthProfit(),
                wallet.getLastYearProfit());
    }

    public List<WalletOutDTOForAll> toOutDTOForAllList(List<Wallet> wallets) {
        return wallets.stream().map(this::toOutDTOForAll).toList();
    }
}
