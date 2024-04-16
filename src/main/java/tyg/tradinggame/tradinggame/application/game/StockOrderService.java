package tyg.tradinggame.tradinggame.application.game;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;
import tyg.tradinggame.tradinggame.application.stock.StockValueService;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.OrderRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.StockOrder;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Wallet;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

import tyg.tradinggame.tradinggame.application.exceptions.PublicIllegalArgumentException;

@Service
public class StockOrderService {

    private final OrderRepository orderRepository;

    private final WalletService walletService;
    private final StockValueService stockValueService;

    public StockOrderService(OrderRepository orderRepository,
            WalletService walletService,
            StockValueService stockValueService) {
        this.orderRepository = orderRepository;
        this.walletService = walletService;
        this.stockValueService = stockValueService;
    }

    public void createStockOrder(StockOrderInDTO stockOrderInDTO) {
        Wallet wallet = walletService.getWalletById(stockOrderInDTO.walletId);
        StockValue stockValue = stockValueService.getStockValueById(stockOrderInDTO.stockValueId);

        if (stockOrderInDTO.expirationGameDate.isBefore(wallet.getGame().getCurrentGameDate())) {
            throw new PublicIllegalArgumentException("Expiration date must be after current game date");
        }

        StockOrder stockOrder = new StockOrder(
                stockOrderInDTO.type,
                stockOrderInDTO.price,
                stockOrderInDTO.quantity,
                stockOrderInDTO.expirationGameDate,
                wallet,
                stockValue);
        orderRepository.save(stockOrder);
        wallet.getStockOrders().add(stockOrder);
    }

    public StockOrderOutDTO toOutDTO(StockOrder stockOrder) {
        return new StockOrderOutDTO(
                stockOrder.getType(),
                stockOrder.getPrice(),
                stockOrder.getQuantity(),
                stockOrder.getCreationGameDate(),
                stockOrder.getExpirationGameDate(),
                stockOrder.getStockValue());
    }

    public static record StockOrderInDTO(
            @NotNull(message = "Order Type is mandatory") OrderTypeEnum type,
            @NotNull(message = "Price is mandatory") double price,
            @NotNull(message = "Quantity is mandatory") Long quantity,
            @NotNull(message = "The order expiration date is mandatory") LocalDate expirationGameDate,
            @NotNull(message = "The wallet id is mandatory") Long walletId,
            @NotNull(message = "The stock value id is mandatory") Long stockValueId) {
    }

    public static record StockOrderOutDTO(
            OrderTypeEnum type,
            double price,
            Long quantity,
            LocalDate creationGameDate,
            LocalDate expirationGameDate,
            StockValue stockValue) {
    }
}
