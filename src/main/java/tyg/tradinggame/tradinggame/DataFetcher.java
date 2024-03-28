package tyg.tradinggame.tradinggame;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataFetcher {
    private final AlphaVantageApiClient alphaVantageApiClient;

    public DataFetcher(AlphaVantageApiClient alphaVantageApiClient) {
        this.alphaVantageApiClient = alphaVantageApiClient;
    }

    @PostConstruct
    public void fetchData() {
        System.out.println("Fetching data...");
        alphaVantageApiClient.fetchData("AAPL");
        System.out.println("Data fetched successfully!");
    }
}
