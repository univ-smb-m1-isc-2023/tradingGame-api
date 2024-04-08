package tyg.tradinggame.tradinggame;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tyg.tradinggame.tradinggame.application.UserRepositoryService;
import tyg.tradinggame.tradinggame.infrastructure.persistence.UserGame;

import java.util.List;

@RestController
@RequestMapping("/")
public class TradingGameController {

    private final UserRepositoryService userService;

    public TradingGameController(UserRepositoryService userService) {
        this.userService = userService;
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
}
