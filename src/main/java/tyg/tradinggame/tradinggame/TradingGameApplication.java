package tyg.tradinggame.tradinggame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradingGameApplication {

	public static void main(String[] args) {
		System.out.println("Démarrage de l'application...");
		SpringApplication.run(TradingGameApplication.class, args);
		System.out.println("Application démarrée avec succès !");
	}
}