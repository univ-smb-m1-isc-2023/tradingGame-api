package tyg.tradinggame.tradinggame.mappers.game;

import java.util.List;

import tyg.tradinggame.tradinggame.dto.game.StockOrderDTOs.StockOrderOutDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;

public class StockOrderMapper {

    public static StockOrderOutDTO toOutDTO(StockOrder stockOrder) {
        return new StockOrderOutDTO(
                stockOrder.getType(),
                stockOrder.getPrice(),
                stockOrder.getQuantity(),
                stockOrder.getCreationGameDate(),
                stockOrder.getExpirationGameDate(),
                stockOrder.getStockValue());
    }

    public static List<StockOrderOutDTO> toOutDTOList(List<StockOrder> stockOrders) {
        return stockOrders.stream().map(StockOrderMapper::toOutDTO).toList();
    }

}
