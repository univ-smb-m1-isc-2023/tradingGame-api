package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.GameTypeEnum;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private GameTypeEnum type;

    @Column(nullable = false)
    private LocalDate initialGameDate;
    @Column(nullable = true)
    private LocalDate finalGameDate;
    @Column(nullable = false)
    private LocalDate currentGameDate;

    @Column(nullable = false)
    private double initialBalance;
    @Column(nullable = false)
    private Duration moveDuration;

    @ManyToOne
    @JsonBackReference
    private Player admin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "game")
    @JsonManagedReference
    private List<Wallet> wallets = new ArrayList<>();

    public Game(
            String title,
            GameTypeEnum type,
            LocalDate initialGameDate,
            LocalDate finalGameDate,
            double initialBalance,
            Duration moveDuration,
            Player admin) {
        this.title = title;
        this.type = type;
        this.initialGameDate = initialGameDate;
        this.finalGameDate = finalGameDate;
        this.currentGameDate = initialGameDate;
        this.initialBalance = initialBalance;
        this.moveDuration = moveDuration;
        this.admin = admin;
    }

    public Game() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getInitialGameDate() {
        return initialGameDate;
    }

    public void setInitialGameDate(LocalDate initialDate) {
        this.initialGameDate = initialDate;
    }

    public LocalDate getFinalGameDate() {
        return finalGameDate;
    }

    public void setFinalGameDate(LocalDate finalDate) {
        this.finalGameDate = finalDate;
    }

    public LocalDate getCurrentGameDate() {
        return currentGameDate;
    }

    public void setCurrentGameDate(LocalDate currentDate) {
        this.currentGameDate = currentDate;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public GameTypeEnum getType() {
        return type;
    }

    public void setType(GameTypeEnum type) {
        this.type = type;
    }

    public Duration getMoveDuration() {
        return moveDuration;
    }

    public void setMoveDuration(Duration moveDuration) {
        this.moveDuration = moveDuration;
    }

    public Player getAdmin() {
        return admin;
    }

    public void setAdmin(Player admin) {
        this.admin = admin;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

}
