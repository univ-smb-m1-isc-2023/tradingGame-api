package tyg.tradinggame.tradinggame;

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

@Component
public class AlphaVantageApiClient {

    @Autowired
    StockValueRepository stockValueRepository;

    @Autowired
    DailyDataRepository dailyDataRepository;

    private static final String API_KEY = "C47GFDHKNBMVKNLL";

    public void fetchData(String symbol) {
        // String symbol = "AAPL"; // Example symbol (Apple Inc.)
        String function = "TIME_SERIES_DAILY"; // Example function (Daily Time Series)

        String apiUrl = "http://localhost:8181/";
        // String apiUrl = "https://www.alphavantage.co/query?function=" + function +
        // "&symbol=" + symbol + "&apikey="
        // + API_KEY + "&outputsize=compact";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        // if (response.getStatusCode().is2xxSuccessful()) {
        // System.out.println("Response: " + response.getBody());
        // } else {
        // System.err.println("Error: " + response.getStatusCode());
        // }

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
                JSONObject dailyDataJson = timeSeriesDataJson.getJSONObject(date);
                Map<String, String> dailyDataMap = new HashMap<>();
                dailyDataJson.keys().forEachRemaining(key -> dailyDataMap.put(key,
                        dailyDataJson.getString(key)));
                dailyDataMap.put("date", date);
                timeSeriesData.put(date, dailyDataMap);
            });

            StockValue stockValue = new StockValue(
                    metaData.get("1. Information"),
                    metaData.get("2. Symbol"),
                    metaData.get("3. Last Refreshed"),
                    metaData.get("4. Output Size"),
                    metaData.get("5. Time Zone"));
            stockValueRepository.save(stockValue);

            for (Map.Entry<String, Map<String, String>> entry : timeSeriesData.entrySet()) {
                Map<String, String> dailyDataMap = entry.getValue();

                DailyData dailyData = new DailyData(
                        dailyDataMap.get("1. open"),
                        dailyDataMap.get("2. high"),
                        dailyDataMap.get("3. low"),
                        dailyDataMap.get("4. close"),
                        dailyDataMap.get("5. volume"),
                        dailyDataMap.get("date"),
                        stockValue);
                stockValue.getDailyData().add(dailyData);
                dailyDataRepository.save(dailyData);
            }

            System.out.println("Parsed StockValue: " + responseBody);
        } else {
            System.err.println("Error: " + response.getStatusCode());
        }
    }

}
