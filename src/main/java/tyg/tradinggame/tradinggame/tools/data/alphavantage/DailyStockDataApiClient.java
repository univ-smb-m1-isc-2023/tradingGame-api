package tyg.tradinggame.tradinggame.tools.data.alphavantage;

import tyg.tradinggame.tradinggame.application.stock.DailyStockDataService;
import tyg.tradinggame.tradinggame.application.stock.StockValueService;
import tyg.tradinggame.tradinggame.application.stock.DailyStockDataService.DailyStockDataBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.application.stock.StockValueService.StockValueInDTO;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

// import jakarta.annotation.PostConstruct;

@Component
public class DailyStockDataApiClient {

    @Autowired
    StockValueService stockValueService;

    @Autowired
    DailyStockDataService dailyStockDataService;

    private static final String function = "TIME_SERIES_DAILY";
    // private static final String function = "TIME_SERIES_DAILY_ADJUSTED";
    private static final String metaDataKey = "Meta Data";
    private static final String dataKey = "Time Series (Daily)";

    private static final List<String> symbols = Arrays.asList("AAPL", "AMZN", "TSLA", "GOOGL", "MSFT", "NFLX", "NVDA",
            "FB", "SHOP", "BA");

    private static final List<String> demo_symbols = Arrays.asList("IBM", "TSCO.LON", "SHOP.TRT", "GPV.TRV", "MBG.DEX",
            "RELIANCE.BSE");

    @Value("${data.dailystock.api.domain}")
    private String API_DOMAIN;

    @Value("${data.dailystock.api.key}")
    private String API_KEY;

    public DailyStockDataApiClient(String API_DOMAIN, String API_KEY) {
        this.API_DOMAIN = API_DOMAIN;
        this.API_KEY = API_KEY;
    }

    public DailyStockDataApiClient() {
    }

    @PostConstruct
    @Transactional
    public void populate() {
        String defualt_api_key = API_KEY;
        String default_api_domain = API_DOMAIN;
        this.API_DOMAIN = "https://www.alphavantage.co";
        this.API_KEY = "demo";

        boolean fetched = false;

        List<String> presentSymbols = stockValueService.getAllStockValues().stream().map(StockValue::getSymbol)
                .toList();
        for (String string : demo_symbols) {
            if (!presentSymbols.contains(string)) {
                fetchAllData(string);
                fetched = true;
            }
        }

        if (fetched) {
            System.out.println("Fetched demo data from AlphaVantage");
        }

        this.API_DOMAIN = default_api_domain;
        this.API_KEY = defualt_api_key;
    }

    @Transactional
    public boolean update() {

        boolean fetched = false;

        List<StockValue> presentStockValues = stockValueService.getAllStockValues();
        for (StockValue stockValue : presentStockValues) {
            if (stockValue.getLastFetched().isBefore(LocalDateTime.now().minus(1, ChronoUnit.DAYS))) {
                if (fetchLastData(stockValue.getSymbol())) {
                    fetched = true;
                }
            }
        }

        if (fetched) {
            System.out.println("Fetched new data from AlphaVantage");
        }

        return fetched;

    }

    public String buildUrl(String domain, String key, String symbol, String outputSize) {
        return domain + "/query?function=" + function + "&symbol=" + symbol + "&outputsize=" + outputSize
                + "&apikey=" + key;
    }

    @Transactional
    public boolean fetchLastData(String symbol) {
        String apiUrl = buildUrl(API_DOMAIN, API_KEY, symbol, "compact");

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
                System.out.println("While parsing: " + responseBody);
                e.printStackTrace();
                return false;
            }

            JSONObject jsonObject = new JSONObject(responseBodyJson);

            JSONObject metaData = jsonObject.getJSONObject(metaDataKey);
            StockValueInDTO stockValueDTO = ResponseParser.toStockValueModel(metaData);

            StockValue stockValue = stockValueService.createOrUpdateStockValue(stockValueDTO);

            JSONObject timeSeries = jsonObject.getJSONObject(dataKey);

            List<DailyStockDataBasicAttributesInDTO> dailyStockDataBasicAttributesDTO = ResponseParser
                    .toDailyStockDataModelList(timeSeries);

            DailyStockDataBasicAttributesInDTO latestDailyStockDataBasicAttributesInDTO = dailyStockDataBasicAttributesDTO
                    .get(0);
            for (int i = 1; i < dailyStockDataBasicAttributesDTO.size(); i++) {
                DailyStockDataBasicAttributesInDTO dailyStockDataBasicAttributesInDTO = dailyStockDataBasicAttributesDTO
                        .get(i);
                if (dailyStockDataBasicAttributesInDTO.date()
                        .isAfter(latestDailyStockDataBasicAttributesInDTO.date())) {
                    latestDailyStockDataBasicAttributesInDTO = dailyStockDataBasicAttributesInDTO;
                }
            }

            dailyStockDataService.createOrUpdateDailyStockData(latestDailyStockDataBasicAttributesInDTO, stockValue);

            System.err.println(
                    "Fetched data for: " + symbol + " date: " + latestDailyStockDataBasicAttributesInDTO.date());
            return true;
        } else {
            System.err.println("Error: " + response.getStatusCode());
            return false;
        }
    }

    @Transactional
    public void fetchAllData(String symbol) {
        String apiUrl = buildUrl(API_DOMAIN, API_KEY, symbol, "full");

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
                System.out.println("While parsing: " + responseBody);
                e.printStackTrace();
                return;
            }

            JSONObject jsonObject = new JSONObject(responseBodyJson);

            JSONObject metaData = jsonObject.getJSONObject(metaDataKey);
            StockValueInDTO stockValueDTO = ResponseParser.toStockValueModel(metaData);

            StockValue stockValue = stockValueService.createOrUpdateStockValue(stockValueDTO);

            JSONObject timeSeries = jsonObject.getJSONObject(dataKey);
            List<DailyStockDataBasicAttributesInDTO> dailyStockDataBasicAttributesDTO = ResponseParser
                    .toDailyStockDataModelList(timeSeries);

            dailyStockDataService.forceWriteStockData(dailyStockDataBasicAttributesDTO, stockValue);

        } else {
            System.err.println("Error: " + response.getStatusCode());
        }
    }

}
