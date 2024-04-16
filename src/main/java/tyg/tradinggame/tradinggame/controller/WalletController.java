package tyg.tradinggame.tradinggame.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    @PostMapping("/wallet/stock_order")
    public void createStockOrder() {
        System.err.println("Creating stock order");
    }
}
