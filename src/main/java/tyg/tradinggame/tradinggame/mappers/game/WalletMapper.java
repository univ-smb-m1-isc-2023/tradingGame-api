package tyg.tradinggame.tradinggame.mappers.game;

import java.util.List;

import tyg.tradinggame.tradinggame.dto.game.WalletDTOs.WalletOutDTOForAll;
import tyg.tradinggame.tradinggame.dto.game.WalletDTOs.WalletOutDTOForOwner;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;

public class WalletMapper {

    public static WalletOutDTOForOwner toOutDTOForOwner(Wallet wallet) {
        return new WalletOutDTOForOwner(
                wallet.getId(),
                wallet.getGame().getId(),
                wallet.getBalance(),
                wallet.getLastMonthProfit(),
                wallet.getLastYearProfit(),
                StockOrderMapper.toOutDTOList(wallet.getStockOrders()));
    }

    public static List<WalletOutDTOForOwner> toOutDTOForOwnerList(List<Wallet> wallets) {
        return wallets.stream().map(WalletMapper::toOutDTOForOwner).toList();
    }

    public static WalletOutDTOForAll toOutDTOForAll(Wallet wallet) {
        return new WalletOutDTOForAll(
                wallet.getOwner().getId(),
                wallet.getOwner().getUsername(),
                wallet.getBalance(),
                wallet.getLastMonthProfit(),
                wallet.getLastYearProfit());
    }

    public static List<WalletOutDTOForAll> toOutDTOForAllList(List<Wallet> wallets) {
        return wallets.stream().map(WalletMapper::toOutDTOForAll).toList();
    }
}
