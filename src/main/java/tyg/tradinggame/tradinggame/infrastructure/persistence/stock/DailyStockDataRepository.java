package tyg.tradinggame.tradinggame.infrastructure.persistence.stock;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyStockDataRepository extends JpaRepository<DailyStockData, Long> {
        List<DailyStockData> findByStockValue_Symbol(String symbol, Sort sort);

        List<DailyStockData> findByStockValue_SymbolAndDateBetween(String symbol, LocalDate startDate,
                        LocalDate endDate,
                        Sort sort);

        DailyStockData findByStockValue_SymbolAndDate(String symbol, LocalDate date);

        void deleteByStockValue_Symbol(String symbol);

        Long countByStockValue_SymbolAndDateBetween(String symbol, LocalDate startDate, LocalDate endDate);

        @Query("SELECT COUNT(DISTINCT d.date) FROM DailyStockData d WHERE d.date BETWEEN :startDate AND :endDate")
        Long countDistinctDatesBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT DISTINCT d.date FROM DailyStockData d WHERE d.date BETWEEN :startDate AND :endDate ORDER BY d.date ASC")
        List<LocalDate> findDistinctDatesBetween(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        List<DailyStockData> findByDate(LocalDate date);

        List<DailyStockData> findByDateAndStockValue(LocalDate date, StockValue stockValue);
}