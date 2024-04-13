package tyg.tradinggame.tradinggame.tools.data.alphavantage;

import tyg.tradinggame.tradinggame.infrastructure.persistence.StockValue;
import tyg.tradinggame.tradinggame.application.DailyStockDataRepositoryService;
import tyg.tradinggame.tradinggame.application.DailyStockDataRepositoryService.DailyStockDataBasicAttributesDTO;
import tyg.tradinggame.tradinggame.application.StockValueRepositoryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class DailyStockDataApiClient {

    @Autowired
    StockValueRepositoryService stockValueRepositoryService;

    @Autowired
    DailyStockDataRepositoryService dailyStockDataRepositoryService;

    private static final String API_KEY = "C47GFDHKNBMVKNLL";

    // @PostConstruct
    public void fetchData(String symbol) {
        // String symbol = "AAPL"; // Example symbol (Apple Inc.)
        String function = "TIME_SERIES_DAILY";
        // String function = "TIME_SERIES_DAILY_ADJUSTED";

        // String apiUrl = "http://localhost:8181/";
        String apiUrl = "https://www.alphavantage.co/query?function=" + function +
                "&symbol=" + symbol + "&apikey="
                + API_KEY + "&outputsize=full";

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

            StockValue stockValue = stockValueRepositoryService.createOrUpdateStockValue(
                    ResponseParser.toStockValueModel(metaData));

            List<DailyStockDataBasicAttributesDTO> dailyStockDataBasicAttributesDTO = new ArrayList<>();

            for (Map.Entry<String, Map<String, String>> entry : timeSeriesData.entrySet()) {
                Map<String, String> dailyStockDataMap = entry.getValue();

                dailyStockDataBasicAttributesDTO.add(
                        ResponseParser.toDailyStockDataBasicAttributesModel(dailyStockDataMap));
            }

            dailyStockDataRepositoryService.forceWriteStockData(dailyStockDataBasicAttributesDTO, stockValue);

            System.out.println("Parsed StockValue: " + responseBody);
        } else {
            System.err.println("Error: " + response.getStatusCode());
        }
    }

}
