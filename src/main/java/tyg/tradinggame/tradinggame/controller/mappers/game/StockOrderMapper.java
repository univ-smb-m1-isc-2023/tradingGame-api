package tyg.tradinggame.tradinggame.controller.mappers.game;

import java.util.List;

import org.springframework.stereotype.Component;

import tyg.tradinggame.tradinggame.controller.dto.game.StockOrderDTOs.StockOrderOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderStatusEnum;

@Component
public class StockOrderMapper {

    private final StockValueMapper stockValueMapper;

    public StockOrderMapper(StockValueMapper stockValueMapper) {
        this.stockValueMapper = stockValueMapper;
    }

    public StockOrderOutDTO toOutDTO(StockOrder stockOrder) {
        OrderStatusEnum status;
        if (stockOrder.isCancelled()) {
            status = OrderStatusEnum.CANCELLED;
        } else if (stockOrder.isExecuted()) {
            status = OrderStatusEnum.EXECUTED;
        } else {
            if (stockOrder.getExpirationGameDate().isBefore(stockOrder.getWallet().getCurrentGameDate())) {
                status = OrderStatusEnum.EXPIRED;
            } else
                status = OrderStatusEnum.PENDING;
        }
        return new StockOrderOutDTO(
                stockOrder.getType(),
                stockOrder.getPrice(),
                stockOrder.getExecutionPrice(),
                stockOrder.getQuantity(),
                stockOrder.getCreationGameDate(),
                stockOrder.getExpirationGameDate(),
                stockValueMapper.toOutDTOForOverview(stockOrder.getStockValue()),
                status);
    }

    public List<StockOrderOutDTO> toOutDTOList(List<StockOrder> stockOrders) {
        return stockOrders.stream().map(this::toOutDTO).toList();
    }

}
