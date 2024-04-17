package tyg.tradinggame.tradinggame.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tyg.tradinggame.tradinggame.application.game.WalletService;
import tyg.tradinggame.tradinggame.dto.game.StockOrderDTOs.StockOrderBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.dto.game.StockOrderDTOs.StockOrderOutDTO;
import tyg.tradinggame.tradinggame.dto.game.WalletDTOs.WalletOutDTOForOwner;

@CrossOrigin\(origins = "*"\)
@RestController
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallet/stock_order")
    public WalletOutDTOForOwner createStockOrder(@RequestBody StockOrderBasicAttributesInDTO stockOrderInDTO) {
        WalletOutDTOForOwner walletOutDTOForOwner = walletService.createStockOrder(stockOrderInDTO);
        return walletOutDTOForOwner;
    }
}
