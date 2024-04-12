package tyg.tradinggame.tradinggame.tools.data.alphavantage;

import tyg.tradinggame.tradinggame.infrastructure.persistence.StockValueRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.DailyStockDataRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.StockValue;
import tyg.tradinggame.tradinggame.infrastructure.persistence.DailyStockData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Component
public class AlphaVantageApiClient {

    @Autowired
    StockValueRepository stockValueRepository;

    @Autowired
    DailyStockDataRepository dailyStockDataRepository;

    private static final String API_KEY = "C47GFDHKNBMVKNLL";

    // @PostConstruct
    public void fetchData(String symbol) {
        // String symbol = "AAPL"; // Example symbol (Apple Inc.)
        String function = "TIME_SERIES_DAILY"; // Example function (Daily Time Series)

        // String apiUrl = "http://localhost:8181/";
        String apiUrl = "https://www.alphavantage.co/query?function=" + function +
                "&symbol=" + symbol + "&apikey="
                + API_KEY + "&outputsize=compact";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            String responseBodyJson;
            try {
                responseBodyJson = objectMapper.writeValueAsString(responseBody);
            } catch (JsonProcessingException e) {
                System.out.println("Parsed StockValue: " + responseBody);
                e.printStackTrace();
                return;
            }

            JSONObject jsonObject = new JSONObject(responseBodyJson);

            Map<String, String> metaData = new HashMap<>();
            JSONObject metaDataJson = jsonObject.getJSONObject("Meta Data");
            metaDataJson.keys().forEachRemaining(key -> metaData.put(key,
                    metaDataJson.getString(key)));

            Map<String, Map<String, String>> timeSeriesData = new HashMap<>();
            JSONObject timeSeriesDataJson = jsonObject.getJSONObject("Time Series (Daily)");

            timeSeriesDataJson.keys().forEachRemaining(date -> {
                JSONObject dailyStockDataJson = timeSeriesDataJson.getJSONObject(date);
                Map<String, String> dailyStockDataMap = new HashMap<>();
                dailyStockDataJson.keys().forEachRemaining(key -> dailyStockDataMap.put(key,
                        dailyStockDataJson.getString(key)));
                dailyStockDataMap.put("date", date);
                timeSeriesData.put(date, dailyStockDataMap);
            });

            StockValue stockValue = new StockValue(
                    metaData.get("1. Information"),
                    metaData.get("2. Symbol"),
                    metaData.get("3. Last Refreshed"),
                    metaData.get("4. Output Size"),
                    metaData.get("5. Time Zone"));
            stockValueRepository.save(stockValue);

            for (Map.Entry<String, Map<String, String>> entry : timeSeriesData.entrySet()) {
                Map<String, String> dailyStockDataMap = entry.getValue();

                DailyStockData dailyStockData = new DailyStockData(
                        dailyStockDataMap.get("1. open"),
                        dailyStockDataMap.get("2. high"),
                        dailyStockDataMap.get("3. low"),
                        dailyStockDataMap.get("4. close"),
                        dailyStockDataMap.get("5. volume"),
                        dailyStockDataMap.get("date"),
                        stockValue);
                stockValue.getDailyStockData().add(dailyStockData);
                dailyStockDataRepository.save(dailyStockData);
            }

            System.out.println("Parsed StockValue: " + responseBody);
        } else {
            System.err.println("Error: " + response.getStatusCode());
        }
    }

}
