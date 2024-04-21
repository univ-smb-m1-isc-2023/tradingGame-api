package tyg.tradinggame.tradinggame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tyg.tradinggame.tradinggame.application.game.WalletService;
import tyg.tradinggame.tradinggame.controller.dto.game.StockOrderDTOs.StockOrderBasicAttributesInDTO;
import tyg.tradinggame.tradinggame.controller.dto.game.WalletDTOs.WalletOutDTOForOwner;
import tyg.tradinggame.tradinggame.controller.dto.stock.StockValueDTOs.StockValueOutDTOForOwner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "*")
@RestController
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/wallet/{id}")
    public WalletOutDTOForOwner walletById(@PathVariable Long id) {
        WalletOutDTOForOwner walletOutDTOForOwner = walletService.getById(id);
        return walletOutDTOForOwner;
    }

    @PostMapping("/wallet/stock_order")
    public WalletOutDTOForOwner createStockOrder(@RequestBody StockOrderBasicAttributesInDTO stockOrderInDTO) {
        WalletOutDTOForOwner walletOutDTOForOwner = walletService.createStockOrder(stockOrderInDTO);
        return walletOutDTOForOwner;
    }

    @GetMapping("/wallet/{id}/stock_value")
    public List<StockValueOutDTOForOwner> getStockValue(@PathVariable Long id) {
        return walletService.getAllStockValuesByWalletId(id);
    }

}
