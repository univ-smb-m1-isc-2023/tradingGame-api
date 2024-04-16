package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderStatusEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.OrderTypeEnum;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "stock_order")
public class StockOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private OrderTypeEnum type;

    private double price;
    private Long quantity;
    private LocalDate creationGameDate;
    private LocalDate expirationGameDate;

    private OrderStatusEnum status;

    @ManyToOne
    @JsonBackReference
    private Wallet wallet;

    @ManyToOne
    @JsonBackReference
    private StockValue stockValue;

    public StockOrder(OrderTypeEnum type, double price, Long quantity, LocalDate expirationDate,
            Wallet wallet, StockValue stockValue) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.creationGameDate = wallet.getCurrentGameDate();
        this.expirationGameDate = expirationDate;
        this.status = OrderStatusEnum.PENDING;
        this.wallet = wallet;
        this.stockValue = stockValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderTypeEnum getType() {
        return type;
    }

    public void setType(OrderTypeEnum type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public LocalDate getCreationGameDate() {
        return creationGameDate;
    }

    public void setCreationGameDate(LocalDate creationGameDate) {
        this.creationGameDate = creationGameDate;
    }

    public LocalDate getExpirationGameDate() {
        return expirationGameDate;
    }

    public void setExpirationGameDate(LocalDate expirationGameDate) {
        this.expirationGameDate = expirationGameDate;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public StockValue getStockValue() {
        return stockValue;
    }

    public void setStockValue(StockValue stockValue) {
        this.stockValue = stockValue;
    }

}
