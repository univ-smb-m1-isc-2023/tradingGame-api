package tyg.tradinggame.tradinggame.controller.dto.stock;

import java.time.LocalDateTime;

public class StockValueDTOs {
        public static record StockValueOutDTOForOverview(
                        Long id,
                        String symbol,
                        String lastRefreshed,
                        LocalDateTime lastFetched) {
        }

        public static record StockValueOutDTOForGame(
                        Long id,
                        String symbol,
                        double price) {
        }

        public static record StockValueOutDTOForOwner(
                        Long id,
                        String symbol,
                        double price,
                        Long quantity) {
        }

}
