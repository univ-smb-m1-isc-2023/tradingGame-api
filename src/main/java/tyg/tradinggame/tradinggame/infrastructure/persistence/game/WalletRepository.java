package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT so FROM StockOrder so WHERE so.wallet = :wallet AND so.wallet.game.currentGameDate <= so.expirationGameDate AND so.cancelled = false AND so.executed = false ")
    List<StockOrder> findPendingOrdersOfAWallet(@Param("wallet") Wallet wallet);

    @Query("SELECT so FROM StockOrder so WHERE so.wallet = :wallet AND so.executed = true ")
    List<StockOrder> findExecutedOrdersOfAWallet(@Param("wallet") Wallet wallet);

    @Query("SELECT COUNT(so) FROM StockOrder so WHERE so.wallet = :wallet AND so.stockValue = :stockValue AND (so.type = tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum.BUY_MARKET OR so.type = tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum.BUY_LIMIT) AND so.executed = true")
    Long countBoughtStocksOfAValue(@Param("wallet") Wallet wallet, @Param("stockValue") StockValue stockValue);

    @Query("SELECT COUNT(so) FROM StockOrder so WHERE so.wallet = :wallet AND so.stockValue = :stockValue AND (so.type = tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum.SELL_MARKET OR so.type = tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum.SELL_LIMIT) AND so.executed = true")
    Long countSoldStocksOfAValue(@Param("wallet") Wallet wallet, @Param("stockValue") StockValue stockValue);

}