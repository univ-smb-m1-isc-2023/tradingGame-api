package tyg.tradinggame.tradinggame.controller.mappers.game;

import org.springframework.stereotype.Component;

import tyg.tradinggame.tradinggame.controller.dto.stock.StockValueDTOs.StockValueOutDTOForOverview;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

@Component
public class StockValueMapper {
    public StockValueOutDTOForOverview toOutDTOForOverview(StockValue stockValue) {
        return new StockValueOutDTOForOverview(
                stockValue.getId(),
                stockValue.getSymbol(),
                stockValue.getLastRefreshed());
    }
}
