package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
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

    @Column(nullable = false)
    private OrderTypeEnum type;

    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private Long quantity;
    @Column(nullable = false)
    private LocalDate creationGameDate;
    @Column(nullable = false)
    private LocalDate expirationGameDate;
    @Column(nullable = false)
    private boolean executed;
    @Column(nullable = false)
    private boolean cancelled;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(nullable = false)
    private Wallet wallet;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(nullable = false)
    private StockValue stockValue;

    public StockOrder(OrderTypeEnum type, double price, Long quantity, LocalDate expirationDate,
            Wallet wallet, StockValue stockValue) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.creationGameDate = wallet.getCurrentGameDate();
        this.expirationGameDate = expirationDate;
        this.executed = false;
        this.cancelled = false;
        this.wallet = wallet;
        this.stockValue = stockValue;
    }

    public StockOrder() {
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

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
