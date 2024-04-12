package tyg.tradinggame.tradinggame.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyStockDataRepository extends JpaRepository<DailyStockData, Long> {
    List<DailyStockData> findByStockValue_Symbol(String symbol);
}