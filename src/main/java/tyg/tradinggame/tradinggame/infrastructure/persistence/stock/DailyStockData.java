package tyg.tradinggame.tradinggame.infrastructure.persistence.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "daily_stock_data")
public class DailyStockData {
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
    @JsonBackReference
    private StockValue stockValue;

    public DailyStockData(double open, double high, double low, double close, double volume, LocalDate date,
            StockValue stockValue) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.date = date;
        this.stockValue = stockValue;
    }

    public DailyStockData() {
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
