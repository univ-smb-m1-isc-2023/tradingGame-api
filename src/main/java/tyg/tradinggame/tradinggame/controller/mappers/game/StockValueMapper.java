package tyg.tradinggame.tradinggame.controller.mappers.game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import tyg.tradinggame.tradinggame.controller.dto.stock.StockValueDTOs.*;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

@Component
public class StockValueMapper {
    public StockValueOutDTOForOverview toOutDTOForOverview(StockValue stockValue) {
        return new StockValueOutDTOForOverview(
                stockValue.getId(),
                stockValue.getSymbol(),
                stockValue.getLastRefreshed());
    }

    public StockValueOutDTOForGame toOutDTOForGame(DailyStockData dailyStockData) {
        return new StockValueOutDTOForGame(
                dailyStockData.getStockValue().getId(),
                dailyStockData.getStockValue().getSymbol(),
                dailyStockData.getClose());
    }

    public List<StockValueOutDTOForGame> toOutDTOsForGame(List<DailyStockData> dailyStockDatas) {
        return dailyStockDatas.stream().map(this::toOutDTOForGame).toList();
    }

    public StockValueOutDTOForOwner toOutDTOForOwner(StockValueWithPriceAndQuantity stockValueWithPriceAndQuantity) {
        return new StockValueOutDTOForOwner(
                stockValueWithPriceAndQuantity.stockValue().getId(),
                stockValueWithPriceAndQuantity.stockValue().getSymbol(),
                stockValueWithPriceAndQuantity.price(),
                stockValueWithPriceAndQuantity.quantity());
    }

    public List<StockValueOutDTOForOwner> toOutDTOsForOwner(
            List<StockValueWithPriceAndQuantity> stockValueWithPriceAndQuantities) {
        return stockValueWithPriceAndQuantities.stream().map(this::toOutDTOForOwner).toList();
    }

    public record StockValueWithPriceAndQuantity(
            StockValue stockValue,
            double price,
            Long quantity) {
    }
}
