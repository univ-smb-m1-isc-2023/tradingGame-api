package tyg.tradinggame.tradinggame;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tyg.tradinggame.tradinggame.application.UserRepositoryService;
import tyg.tradinggame.tradinggame.application.DailyStockDataRepositoryService;
import tyg.tradinggame.tradinggame.infrastructure.persistence.DailyStockData;
import tyg.tradinggame.tradinggame.infrastructure.persistence.UserGame;
import tyg.tradinggame.tradinggame.tools.data.alphavantage.AlphaVantageApiClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/")
public class TradingGameController {

    private final UserRepositoryService userService;
    private final DailyStockDataRepositoryService dailyStockDataRepositoryService;
    private final AlphaVantageApiClient alphaVantageApiClient;

    private static final LocalDateTime DEPLOYMENT_TIME = LocalDateTime.now();

    public TradingGameController(UserRepositoryService userService,
            DailyStockDataRepositoryService dailyStockDataRepositoryService,
            AlphaVantageApiClient alphaVantageApiClient) {
        this.userService = userService;
        this.dailyStockDataRepositoryService = dailyStockDataRepositoryService;
        this.alphaVantageApiClient = alphaVantageApiClient;
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello world!";
    }

    @GetMapping("/create")
    public UserGame create() {
        return userService.create("enzo");
    }

    @GetMapping("/show")
    public List<UserGame> show() {
        return userService.users();
    }

    @GetMapping("/timestats")
    public Map<String, Object> timeStats() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDeploymentTime = DEPLOYMENT_TIME.format(formatter);

        Duration duration = Duration.between(DEPLOYMENT_TIME, LocalDateTime.now());
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        Map<String, Object> response = new HashMap<>();
        response.put("deployment_time", formattedDeploymentTime);
        response.put("time_format", "dd-MM-yyyy HH:mm:ss");
        response.put("elapsed_time",
                String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds));

        return response;
    }

    @GetMapping("/stock/{symbol}")
    public List<DailyStockData> dailyStockDatas(@PathVariable String symbol) {
        if (symbol == null || symbol.isEmpty() || "ALL".equals(symbol)) {
            System.err.println("Getting all stock data");
            return dailyStockDataRepositoryService.getAll();
        } else {
            System.err.println("Getting stock data for symbol: " + symbol);
            return dailyStockDataRepositoryService.getAllBySymbol(symbol);
        }
    }

    @GetMapping("/populate/{symbol}")
    public String populateStockData(@PathVariable String symbol) {
        alphaVantageApiClient.fetchData(symbol);
        return "Data fetched and populated";
    }
}
