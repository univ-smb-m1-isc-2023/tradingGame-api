package tyg.tradinggame.tradinggame;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tyg.tradinggame.tradinggame.application.UserRepositoryService;
import tyg.tradinggame.tradinggame.infrastructure.persistence.UserGame;

import java.util.List;

@RestController
@RequestMapping("/")
public class HelloWorldController {

    private final UserRepositoryService userService;

    public HelloWorldController(UserRepositoryService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello woorldo !";
    }


    @GetMapping("/test")
    public String testWorld() {
    return "trading game";
}


    @GetMapping("/create")
    public void create() {
        userService.create("enzo");
    }

    @GetMapping("/show")
    public List<UserGame> show() {
        return userService.users();
    }
}

