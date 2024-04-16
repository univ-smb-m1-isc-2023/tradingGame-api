package tyg.tradinggame.tradinggame.application.game;

import java.util.List;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.dto.game.StockOrderDTOs.StockOrderInDTO;
import tyg.tradinggame.tradinggame.dto.game.StockOrderDTOs.StockOrderOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.OrderRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;

@Service
public class StockOrderService {

    private final OrderRepository orderRepository;

    public StockOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

}
