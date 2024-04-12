package tyg.tradinggame.tradinggame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tyg.tradinggame.tradinggame.infrastructure.persistence.DailyStockData;
import tyg.tradinggame.tradinggame.application.DailyStockDataRepositoryService;
import tyg.tradinggame.tradinggame.tools.data.alphavantage.DailyStockDataApiClient;

import java.util.List;

@RestController
public class StockController {

    private final DailyStockDataRepositoryService dailyStockDataRepositoryService;
    private final DailyStockDataApiClient dailyStockDataApiClient;

    public StockController(DailyStockDataRepositoryService dailyStockDataRepositoryService,
            DailyStockDataApiClient dailyStockDataApiClient) {
        this.dailyStockDataRepositoryService = dailyStockDataRepositoryService;
        this.dailyStockDataApiClient = dailyStockDataApiClient;
    }

    @GetMapping("/stock")
    public List<DailyStockData> allDailyStockDatas() {
        System.err.println("Getting all stock data");
        return dailyStockDataRepositoryService.getAll();
    }

    @GetMapping("/stock/{symbol}")
    public List<DailyStockData> dailyStockDatasBySymbol(@PathVariable String symbol) {
        if ("ALL".equals(symbol)) {
            System.err.println("Getting all stock data");
            return dailyStockDataRepositoryService.getAll();
        } else {
            System.err.println("Getting stock data for symbol: " + symbol);
            return dailyStockDataRepositoryService.getAllBySymbol(symbol);
        }
    }

    @GetMapping("/stock/{symbol}/{startDate}/{endDate}")
    public ResponseEntity<?> dailyStockDatasBySymbolAndDate(@PathVariable String symbol,
            @PathVariable String startDate, @PathVariable String endDate) {
        try {
            dailyStockDataRepositoryService.validateDatePeriod(startDate, endDate);

            System.err
                    .println("Getting stock data for symbol: " + symbol + " between " + startDate + " and " + endDate);
            List<DailyStockData> data = dailyStockDataRepositoryService.getAllBySymbolAndDate(symbol, startDate,
                    endDate);
            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/populate/{symbol}")
    public ResponseEntity<String> populateStockData(@PathVariable String symbol) {
        dailyStockDataApiClient.fetchData(symbol);

        return ResponseEntity.ok("Stock data populated for symbol: " + symbol);
    }
}