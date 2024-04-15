package tyg.tradinggame.tradinggame.infrastructure.persistence.stock;

import org.springframework.data.repository.CrudRepository;

public interface StockValueRepository extends CrudRepository<StockValue, Long> {
    StockValue findBySymbol(String symbol);

}
