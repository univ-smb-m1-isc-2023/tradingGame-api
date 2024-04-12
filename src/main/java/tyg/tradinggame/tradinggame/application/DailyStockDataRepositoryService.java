package tyg.tradinggame.tradinggame.application;

import tyg.tradinggame.tradinggame.infrastructure.persistence.DailyStockData;
import tyg.tradinggame.tradinggame.infrastructure.persistence.DailyStockDataRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyStockDataRepositoryService {

    private final DailyStockDataRepository dailyStockDataRepository;

    public DailyStockDataRepositoryService(DailyStockDataRepository repository) {
        this.dailyStockDataRepository = repository;
    }

    public List<DailyStockData> getAll() {
        return dailyStockDataRepository.findAll();
    }

    public List<DailyStockData> getAllBySymbol(String symbol) {
        return dailyStockDataRepository.findByStockValue_Symbol(symbol);
    }

}
