package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(nullable = false)
    private Player owner;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(nullable = false)
    private Game game;

    @OneToMany(mappedBy = "wallet")
    @JsonBackReference
    private List<StockOrder> stockOrders = new ArrayList<>();

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private double lastMonthProfit;

    @Column(nullable = false)
    private double lastYearProfit;

    public Wallet(Player owner, Game game, double balance) {
        this.balance = balance;
        this.lastMonthProfit = 0;
        this.lastYearProfit = 0;
        this.owner = owner;
        this.game = game;
    }

    public Wallet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getLastMonthProfit() {
        return lastMonthProfit;
    }

    public void setLastMonthProfit(double lastMonthProfit) {
        this.lastMonthProfit = lastMonthProfit;
    }

    public double getLastYearProfit() {
        return lastYearProfit;
    }

    public void setLastYearProfit(double lastYearProfit) {
        this.lastYearProfit = lastYearProfit;
    }

    public LocalDate getCurrentGameDate() {
        return game.getCurrentGameDate();
    }

}
