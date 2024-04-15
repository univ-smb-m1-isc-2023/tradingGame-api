package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<StockOrder, Long> {

}
