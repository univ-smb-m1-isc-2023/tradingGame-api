package tyg.tradinggame.tradinggame.application.stock;

import org.springframework.stereotype.Service;

import tyg.tradinggame.tradinggame.application.exceptions.PublicIllegalArgumentException;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockData;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.DailyStockDataRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.stock.StockValue;

import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyStockDataService {

    private final DailyStockDataRepository dailyStockDataRepository;

    public DailyStockDataService(DailyStockDataRepository repository) {
        this.dailyStockDataRepository = repository;
    }

    public List<DailyStockData> getAll() {
        return dailyStockDataRepository.findAll();
    }

    public List<DailyStockData> getAllBySymbol(String symbol) {
        Sort sort = Sort.by(Sort.Direction.ASC, "date");
        return dailyStockDataRepository.findByStockValue_Symbol(symbol, sort);
    }

    public List<DailyStockData> getAllBySymbolAndDate(String symbol, String startDate, String endDate) {
        Sort sort = Sort.by(Sort.Direction.ASC, "date");
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        return dailyStockDataRepository.findByStockValue_SymbolAndDateBetween(
                symbol,
                startLocalDate,
                endLocalDate,
                sort);
    }

    public DailyStockData createIfNotExist(DailyStockDataInDTO dailyStockDataDTO) {

        DailyStockData existingDailyStockData = dailyStockDataRepository.findByStockValue_SymbolAndDate(
                dailyStockDataDTO.stockValue.getSymbol(),
                dailyStockDataDTO.date);
        if (existingDailyStockData != null) {
            return existingDailyStockData;
        } else {
            DailyStockData dailyStockData = new DailyStockData(
                    dailyStockDataDTO.open,
                    dailyStockDataDTO.high,
                    dailyStockDataDTO.low,
                    dailyStockDataDTO.close,
                    dailyStockDataDTO.volume,
                    dailyStockDataDTO.date,
                    dailyStockDataDTO.stockValue);
            dailyStockDataRepository.save(dailyStockData);
            return dailyStockData;
        }
    }

    public void forceWriteStockData(List<DailyStockDataBasicAttributesInDTO> dailyStockDataBasicAttributesDTOList,
            StockValue stockValue) {
        dailyStockDataRepository.deleteByStockValue_Symbol(stockValue.getSymbol());
        List<DailyStockData> dailyStockDataList = new ArrayList<>();
        for (DailyStockDataBasicAttributesInDTO dailyStockDataBasicAttributesDTO : dailyStockDataBasicAttributesDTOList) {
            DailyStockData dailyStockData = new DailyStockData(
                    dailyStockDataBasicAttributesDTO.open,
                    dailyStockDataBasicAttributesDTO.high,
                    dailyStockDataBasicAttributesDTO.low,
                    dailyStockDataBasicAttributesDTO.close,
                    dailyStockDataBasicAttributesDTO.volume,
                    dailyStockDataBasicAttributesDTO.date,
                    stockValue);
            dailyStockDataList.add(dailyStockData);
        }
        dailyStockDataRepository.saveAll(dailyStockDataList);
        stockValue.setDailyStockData(dailyStockDataList);
    }

    public void validateDatePeriod(String startDate, String endDate) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
            LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

            if (startLocalDate.isAfter(endLocalDate)) {
                throw new PublicIllegalArgumentException("Start date must be before end date");
            }
        } catch (DateTimeParseException e) {
            throw new PublicIllegalArgumentException("Invalid date format. Please use the format yyyy-MM-dd.", e);
        }
    }

    public List<LocalDate> findDistinctDatesBetween(LocalDate startDate, LocalDate endDate) {
        return dailyStockDataRepository.findDistinctDatesBetween(startDate, endDate);
    }

    public Long countDistinctDatesBetween(LocalDate startDate, LocalDate endDate) {
        return dailyStockDataRepository.countDistinctDatesBetween(startDate, endDate);
    }

    public record DailyStockDataBasicAttributesInDTO(
            double open,
            double high,
            double low,
            double close,
            double volume,
            LocalDate date) {
    }

    public record DailyStockDataInDTO(
            double open,
            double high,
            double low,
            double close,
            double volume,
            LocalDate date,
            StockValue stockValue) {
    }
}
