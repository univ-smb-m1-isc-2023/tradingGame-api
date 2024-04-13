package tyg.tradinggame.tradinggame.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyStockDataRepository extends JpaRepository<DailyStockData, Long> {
    List<DailyStockData> findByStockValue_Symbol(String symbol, Sort sort);

    List<DailyStockData> findByStockValue_SymbolAndDateBetween(String symbol, LocalDate startDate, LocalDate endDate,
            Sort sort);

    DailyStockData findByStockValue_SymbolAndDate(String symbol, LocalDate date);

    void deleteByStockValue_Symbol(String symbol);

}