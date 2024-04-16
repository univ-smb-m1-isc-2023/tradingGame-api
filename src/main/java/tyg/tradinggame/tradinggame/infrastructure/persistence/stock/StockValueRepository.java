package tyg.tradinggame.tradinggame.infrastructure.persistence.stock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockValueRepository extends JpaRepository<StockValue, Long> {
    StockValue findBySymbol(String symbol);

}
