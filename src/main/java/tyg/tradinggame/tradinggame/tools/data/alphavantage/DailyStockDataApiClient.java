package tyg.tradinggame.tradinggame.tools.data.alphavantage;

import tyg.tradinggame.tradinggame.infrastructure.persistence.StockValue;
import tyg.tradinggame.tradinggame.application.DailyStockDataRepositoryService;
import tyg.tradinggame.tradinggame.application.DailyStockDataRepositoryService.DailyStockDataBasicAttributesDTO;
import tyg.tradinggame.tradinggame.application.StockValueRepositoryService;
import tyg.tradinggame.tradinggame.application.StockValueRepositoryService.StockValueDTO;

import java.util.Arrays;
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
import org.springframework.beans.factory.annotation.Value;
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

    private static final String function = "TIME_SERIES_DAILY";
    // private static final String function = "TIME_SERIES_DAILY_ADJUSTED";
    private static final String metaDataKey = "Meta Data";
    private static final String dataKey = "Time Series (Daily)";

    private static final List<String> symbols = Arrays.asList("AAPL", "AMZN", "TSLA", "GOOGL", "MSFT", "NFLX", "NVDA",
            "FB", "SHOP", "BA");

    private static final List<String> demo_symbols = Arrays.asList("IBM", "TSCO.LON", "SHOP.TRT", "GPV.TRV", "MBG.DEX",
            "RELIANCE.BSE");

    @Value("${data.dailystock.api.domain}")
    private String apiDomain;

    @Value("${data.dailystock.api.key}")
    private String API_KEY;

    public DailyStockDataApiClient(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public DailyStockDataApiClient() {
    }

    // @PostConstruct
    public void fetchData(String symbol) {

        String apiUrl = this.apiDomain + "/query?function=" + function +
                "&symbol=" + symbol + "&outputsize=full" + "&apikey="
                + API_KEY;

        System.err.println("Fetching data from: " + apiUrl);

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

            JSONObject metaData = jsonObject.getJSONObject(metaDataKey);
            StockValueDTO stockValueDTO = ResponseParser.toStockValueModel(metaData);

            StockValue stockValue = stockValueRepositoryService.createOrUpdateStockValue(stockValueDTO);

            JSONObject timeSeries = jsonObject.getJSONObject(dataKey);
            List<DailyStockDataBasicAttributesDTO> dailyStockDataBasicAttributesDTO = ResponseParser
                    .toDailyStockDataModelList(timeSeries);

            dailyStockDataRepositoryService.forceWriteStockData(dailyStockDataBasicAttributesDTO, stockValue);

            System.out.println("Parsed StockValue: " + responseBody);
        } else {
            System.err.println("Error: " + response.getStatusCode());
        }
    }

}
