package tyg.tradinggame.tradinggame.application.stock;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValueRepository;

@Service
public class StockValueRepositoryService {

    private final StockValueRepository stockValueRepository;

    public StockValueRepositoryService(StockValueRepository stockValueRepository) {
        this.stockValueRepository = stockValueRepository;
    }

    public StockValue createOrUpdateStockValue(StockValueInDTO stackValueDTO) {

        StockValue existingStockValue = stockValueRepository.findBySymbol(stackValueDTO.symbol);
        if (existingStockValue != null) {
            existingStockValue.setInformation(stackValueDTO.information);
            existingStockValue.setLastRefreshed(stackValueDTO.lastRefreshed);
            existingStockValue.setOutputSize(stackValueDTO.outputSize);
            existingStockValue.setTimeZone(stackValueDTO.timeZone);
            return stockValueRepository.save(existingStockValue);
        } else {
            StockValue stockValue = new StockValue(
                    stackValueDTO.information, stackValueDTO.symbol,
                    stackValueDTO.lastRefreshed, stackValueDTO.outputSize, stackValueDTO.timeZone);
            return stockValueRepository.save(stockValue);
        }
    }

    public record StockValueInDTO(String information, String symbol, String lastRefreshed, String outputSize,
            String timeZone) {
    }
}
