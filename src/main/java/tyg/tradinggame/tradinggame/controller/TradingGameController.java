package tyg.tradinggame.tradinggame.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tyg.tradinggame.tradinggame.application.game.PlayerService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "https://tradinggame-web.oups.net")
@RestController
@RequestMapping("/")
public class TradingGameController {

    private final PlayerService playerService;
    private static final LocalDateTime DEPLOYMENT_TIME = LocalDateTime.now();

    public TradingGameController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello world!";
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

}
