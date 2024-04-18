package tyg.tradinggame.tradinggame.controller.mappers.game;

import tyg.tradinggame.tradinggame.controller.dto.stock.StockValueDTOs.StockValueOutDTOForOverview;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

public class StockValueMapper {
    public static StockValueOutDTOForOverview toOutDTOForOverview(StockValue stockValue) {
        return new StockValueOutDTOForOverview(
                stockValue.getId(),
                stockValue.getSymbol(),
                stockValue.getLastRefreshed());
    }
}
