package tyg.tradinggame.tradinggame.application;

import tyg.tradinggame.tradinggame.infrastructure.persistence.DailyStockData;
import tyg.tradinggame.tradinggame.infrastructure.persistence.DailyStockDataRepository;
import tyg.tradinggame.tradinggame.infrastructure.persistence.StockValue;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyStockDataRepositoryService {

    private final DailyStockDataRepository dailyStockDataRepository;

    public DailyStockDataRepositoryService(DailyStockDataRepository repository) {
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

    public DailyStockData createIfNotExist(DailyStockDataDTO dailyStockDataDTO) {

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

    public void forceWriteStockData(List<DailyStockDataBasicAttributesDTO> dailyStockDataBasicAttributesDTOList,
            StockValue stockValue) {
        dailyStockDataRepository.deleteByStockValue_Symbol(stockValue.getSymbol());
        List<DailyStockData> dailyStockDataList = new ArrayList<>();
        for (DailyStockDataBasicAttributesDTO dailyStockDataBasicAttributesDTO : dailyStockDataBasicAttributesDTOList) {
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
                throw new IllegalArgumentException("Start date must be before end date");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use the format yyyy-MM-dd.", e);
        }
    }

    public record DailyStockDataBasicAttributesDTO(
            double open,
            double high,
            double low,
            double close,
            double volume,
            LocalDate date) {
    }

    public record DailyStockDataDTO(
            double open,
            double high,
            double low,
            double close,
            double volume,
            LocalDate date,
            StockValue stockValue) {
    }
}
