package tyg.tradinggame.tradinggame.mappers.game;

import java.util.List;

import tyg.tradinggame.tradinggame.dto.game.StockOrderDTOs.StockOrderOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderStatusEnum;

public class StockOrderMapper {

    public static StockOrderOutDTO toOutDTO(StockOrder stockOrder) {
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
                stockOrder.getQuantity(),
                stockOrder.getCreationGameDate(),
                stockOrder.getExpirationGameDate(),
                StockValueMapper.toOutDTOForOverview(stockOrder.getStockValue()),
                status);
    }

    public static List<StockOrderOutDTO> toOutDTOList(List<StockOrder> stockOrders) {
        return stockOrders.stream().map(StockOrderMapper::toOutDTO).toList();
    }

}
