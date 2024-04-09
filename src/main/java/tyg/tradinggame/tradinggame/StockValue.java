package tyg.tradinggame.tradinggame;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_value")
public class StockValue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String information;
    private String symbol;
    private String lastRefreshed;
    private String outputSize;
    private String timeZone;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "stockValue")
    private List<DailyData> dailyData = new ArrayList<>();

    public StockValue(String information, String symbol, String lastRefreshed, String outputSize, String timeZone) {
        this.information = information;
        this.symbol = symbol;
        this.lastRefreshed = lastRefreshed;
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

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<DailyData> getDailyData() {
        return dailyData;
    }

    public void setDailyData(List<DailyData> dailyData) {
        this.dailyData = dailyData;
    }
}
