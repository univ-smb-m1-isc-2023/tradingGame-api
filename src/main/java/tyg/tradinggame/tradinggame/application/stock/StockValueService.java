package tyg.tradinggame.tradinggame.application.stock;

import java.util.List;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.application.exceptions.PublicEntityNotFoundException;
import tyg.tradinggame.tradinggame.controller.dto.stock.StockValueDTOs.StockValueOutDTOForOverview;
import tyg.tradinggame.tradinggame.controller.mappers.game.StockValueMapper;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValueRepository;

@Service
public class StockValueService {

    private final StockValueRepository stockValueRepository;
    private final StockValueMapper stockValueMapper;

    public StockValueService(StockValueRepository stockValueRepository,
            StockValueMapper stockValueMapper) {
        this.stockValueRepository = stockValueRepository;
        this.stockValueMapper = stockValueMapper;
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

    public StockValue getStockValueById(Long stockValueId) {
        return stockValueRepository.findById(stockValueId)
                .orElseThrow(() -> new PublicEntityNotFoundException("StockValue not found with id " + stockValueId));
    }

    public List<StockValueOutDTOForOverview> getAll() {
        List<StockValue> stockValues = stockValueRepository.findAll();
        return stockValues.stream().map(stockValue -> stockValueMapper.toOutDTOForOverview(stockValue)).toList();
    }

    public List<StockValue> getAllStockValues() {
        return stockValueRepository.findAll();
    }
}
