package tyg.tradinggame.tradinggame.tools.data.alphavantage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tyg.tradinggame.tradinggame.application.DailyStockDataRepositoryService;
import tyg.tradinggame.tradinggame.application.DailyStockDataRepositoryService.DailyStockDataBasicAttributesDTO;
import tyg.tradinggame.tradinggame.application.StockValueRepositoryService;
import tyg.tradinggame.tradinggame.infrastructure.persistence.StockValue;

public class ResponseParser {

    static protected StockValueRepositoryService.StockValueDTO toStockValueModel(Map<String, String> metaData) {
        return new StockValueRepositoryService.StockValueDTO(
                metaData.get("1. Information"),
                metaData.get("2. Symbol"),
                metaData.get("3. Last Refreshed"),
                metaData.get("4. Output Size"),
                metaData.get("5. Time Zone"));
    }

    static protected DailyStockDataRepositoryService.DailyStockDataDTO toDailyStockDataModel(
            Map<String, String> dailyStockDataMap, StockValue stockValue) {
        return new DailyStockDataRepositoryService.DailyStockDataDTO(
                Double.parseDouble(dailyStockDataMap.get("1. open")),
                Double.parseDouble(dailyStockDataMap.get("2. high")),
                Double.parseDouble(dailyStockDataMap.get("3. low")),
                Double.parseDouble(dailyStockDataMap.get("4. close")),
                Double.parseDouble(dailyStockDataMap.get("5. volume")),
                LocalDate.parse(dailyStockDataMap.get("date")),
                stockValue);
    }

    static protected DailyStockDataBasicAttributesDTO toDailyStockDataBasicAttributesModel(
            Map<String, String> dailyStockDataMap) {
        return new DailyStockDataRepositoryService.DailyStockDataBasicAttributesDTO(
                Double.parseDouble(dailyStockDataMap.get("1. open")),
                Double.parseDouble(dailyStockDataMap.get("2. high")),
                Double.parseDouble(dailyStockDataMap.get("3. low")),
                Double.parseDouble(dailyStockDataMap.get("4. close")),
                Double.parseDouble(dailyStockDataMap.get("5. volume")),
                LocalDate.parse(dailyStockDataMap.get("date")));
    }

}
