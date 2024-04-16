package tyg.tradinggame.tradinggame.tools.data.alphavantage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import tyg.tradinggame.tradinggame.application.stock.DailyStockDataService.DailyStockDataBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.application.stock.StockValueService.StockValueInDTO;

public class ResponseParser {

    static protected StockValueInDTO toStockValueModel(JSONObject metaData) {
        return new StockValueInDTO(
                metaData.getString("1. Information"),
                metaData.getString("2. Symbol"),
                metaData.getString("3. Last Refreshed"),
                metaData.getString("4. Output Size"),
                metaData.getString("5. Time Zone"));
    }

    static protected List<DailyStockDataBasicAttributesInDTO> toDailyStockDataModelList(JSONObject timeSeries) {
        List<DailyStockDataBasicAttributesInDTO> dailyStockDataList = new ArrayList<>();

        for (String dateString : timeSeries.keySet()) {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
            JSONObject dailyData = timeSeries.getJSONObject(dateString);

            DailyStockDataBasicAttributesInDTO dailyStockData = new DailyStockDataBasicAttributesInDTO(
                    dailyData.getDouble("1. open"),
                    dailyData.getDouble("2. high"),
                    dailyData.getDouble("3. low"),
                    dailyData.getDouble("4. close"),
                    dailyData.getLong("5. volume"),
                    date);
            dailyStockDataList.add(dailyStockData);
        }
        return dailyStockDataList;
    }

}
