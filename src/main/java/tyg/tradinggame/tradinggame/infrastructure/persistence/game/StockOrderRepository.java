package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {

    @Query("SELECT so FROM StockOrder so WHERE so.wallet.game = :game AND so.wallet.game.currentGameDate <= so.expirationGameDate AND so.cancelled = false AND so.executed = false ORDER BY so.creationGameDate ASC")
    List<StockOrder> findPendingOrdersForGame(@Param("game") Game game);

}