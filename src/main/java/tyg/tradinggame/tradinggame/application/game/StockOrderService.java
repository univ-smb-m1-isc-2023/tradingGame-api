package tyg.tradinggame.tradinggame.application.game;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrderRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;

@Service
public class StockOrderService {

    private final StockOrderRepository stockOrderRepository;

    public StockOrderService(StockOrderRepository stockOrderRepository) {
        this.stockOrderRepository = stockOrderRepository;
    }

    public StockOrder save(StockOrder stockOrder) {
        return stockOrderRepository.save(stockOrder);
    }

}
