package tyg.tradinggame.tradinggame;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloWorldController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello woorldo !";
    }


    @GetMapping("/test")
    public String testWorld() {
    return "trading game";
}
}
