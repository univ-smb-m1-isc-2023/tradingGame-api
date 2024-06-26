package tyg.tradinggame.tradinggame.infrastructure.persistence.stock;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "stock_value")
public class StockValue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String information;

    @Column(unique = true)
    private String symbol;

    private String lastRefreshed;

    private LocalDateTime lastFetched;
    private String outputSize;
    private String timeZone;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "stockValue")
    @JsonManagedReference
    private List<DailyStockData> dailyStockData = new ArrayList<>();

    public StockValue(String information, String symbol, String lastRefreshed, String outputSize, String timeZone) {
        this.information = information;
        this.symbol = symbol;
        this.lastRefreshed = lastRefreshed;
        this.lastFetched = LocalDateTime.now();
        this.outputSize = outputSize;
        this.timeZone = timeZone;
    }

    public StockValue() {
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public LocalDateTime getLastFetched() {
        if (lastFetched == null) {
            return LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        }
        return lastFetched;
    }

    public void setLastFetched(LocalDateTime lastFetched) {
        this.lastFetched = lastFetched;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(String lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }

    public String getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(String outputSize) {
        this.outputSize = outputSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<DailyStockData> getDailyStockData() {
        return dailyStockData;
    }

    public void setDailyStockData(List<DailyStockData> dailyStockData) {
        this.dailyStockData = dailyStockData;
    }
}
