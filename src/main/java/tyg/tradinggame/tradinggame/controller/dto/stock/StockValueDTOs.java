package tyg.tradinggame.tradinggame.controller.dto.stock;

public class StockValueDTOs {
    public static record StockValueOutDTOForOverview(
            Long id,
            String symbol,
            String lastRefreshed) {
    }
}
