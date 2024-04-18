package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT so FROM StockOrder so WHERE so.wallet = :wallet AND so.wallet.game.currentGameDate <= so.expirationGameDate AND so.cancelled = false AND so.executed = false ")
    List<StockOrder> findPendingOrdersOfAWallet(@Param("wallet") Wallet wallet);

    @Query("SELECT so FROM StockOrder so WHERE so.wallet = :wallet AND so.executed = true ")
    List<StockOrder> findExecutedOrdersOfAWallet(@Param("wallet") Wallet wallet);

}