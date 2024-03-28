package tyg.tradinggame.tradinggame;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
@Table(name = "daily_data")
public class DailyData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private LocalDate date;
    @ManyToOne
    private StockValue stockValue;

    public DailyData(double open, double high, double low, double close, double volume, LocalDate date,
            StockValue stockValue) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.date = date;
        this.stockValue = stockValue;
    }

    public DailyData() {
    }

    public DailyData(String open, String high, String low, String close, String volume, String date,
            StockValue stockValue) {
        this.open = Double.parseDouble(open);
        this.high = Double.parseDouble(high);
        this.low = Double.parseDouble(low);
        this.close = Double.parseDouble(close);
        this.volume = Double.parseDouble(volume);
        this.date = LocalDate.parse(date); // yyyy-MM-dd
        this.stockValue = stockValue;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public StockValue getStockValue() {
        return stockValue;
    }

    public void setStockValue(StockValue stockValue) {
        this.stockValue = stockValue;
    }
}
