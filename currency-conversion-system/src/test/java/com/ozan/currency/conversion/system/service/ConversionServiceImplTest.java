package com.ozan.currency.conversion.system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.ozan.currency.conversion.system.dto.ConversionDTO;
import com.ozan.currency.conversion.system.dto.ConversionHistoryDTO;
import com.ozan.currency.conversion.system.exception.ConversionNotFoundException;
import com.ozan.currency.conversion.system.exception.CurrencyNotFoundException;
import com.ozan.currency.conversion.system.mapper.BeanMapper;
import com.ozan.currency.conversion.system.model.Conversion;
import com.ozan.currency.conversion.system.repository.ConversionRepository;
import com.ozan.currency.conversion.system.util.helper.DateHelper;

@ExtendWith(MockitoExtension.class)
public class ConversionServiceImplTest {

	@InjectMocks
	private ConversionServiceImpl service;
	@Mock
	private ExchangeRateService exchangeRateService;
	@Mock
	private ConversionRepository conversionRepository;
	@Mock
	private BeanMapper mapper;

	@Test
	public void convert_shouldReturnConversionDTOWithCorrectTransactionIdAndTargetAmount() {

		happyPathExchangeRateServiceMock();
		happyPathConversionRepositorySaveMock();
		when(mapper.entityToConversionDTO(any(Conversion.class)))
				.thenReturn(new ConversionDTO(TRANSACTION_ID, TARGET_CURRENCY));

		ConversionDTO dto = service.convert(AMOUNT, CURRENCY_TRY, CURRENCY_USD);
		assertThat(dto).isNotNull();
		assertThat(dto.getTargetAmount()).isEqualTo(TARGET_CURRENCY);
		assertThat(dto.getTransactionId()).isEqualTo(TRANSACTION_ID);
	}

	@Test
	public void convert_shouldInvokeConversionProviderServiceOnce() {

		happyPathExchangeRateServiceMock();
		happyPathConversionRepositorySaveMock();

		service.convert(AMOUNT, CURRENCY_TRY, CURRENCY_USD);
		verify(exchangeRateService, times(1)).getExchangeRate(CURRENCY_TRY, CURRENCY_USD);
	}

	@Test
	public void convert_shouldNotInvokeConversionRepositoryAndMapper_WhenExchangeRateServiceThrowsException() {

		when(exchangeRateService.getExchangeRate(CURRENCY_TRY, CURRENCY_USD)).thenThrow(CurrencyNotFoundException.class);
		
		Assertions.assertThrows(CurrencyNotFoundException.class, () -> {

			service.convert(AMOUNT, CURRENCY_TRY, CURRENCY_USD);
		});

		verify(conversionRepository, times(0)).save(any(Conversion.class));
		verify(mapper, times(0)).entityToConversionDTO(any(Conversion.class));
	}

	@Test
	public void convert_shouldNotInvokeMapper_WhenRepositoryThrowsException() {

		happyPathExchangeRateServiceMock();
		when(conversionRepository.save(any(Conversion.class))).thenThrow(new RuntimeException());

		Assertions.assertThrows(Exception.class, () -> {

			service.convert(AMOUNT, CURRENCY_USD, CURRENCY_TRY);
		});

		verify(mapper, times(0)).entityToConversionDTO(any(Conversion.class));
	}

	@Test
	public void getConversionByTransactionId_ShouldReturnSingleElementList() {

		Conversion conversion = happyPathConversionRepositoryFindByIdMock();
		String conversionTime = happyPathMapperMock(conversion);

		List<ConversionHistoryDTO> conversionHistoryDtos = service.getConversionByTransactionId(TRANSACTION_ID);

		assertThat(conversionHistoryDtos).isNotNull();
		assertThat(conversionHistoryDtos.size()).isEqualTo(1);
		assertThat(conversionHistoryDtos.get(0)).isNotNull();
		assertThat(conversionHistoryDtos.get(0).getTransactionId()).isEqualTo(TRANSACTION_ID);
		assertThat(conversionHistoryDtos.get(0).getTransactionTime()).isEqualTo(conversionTime);
		assertThat(conversionHistoryDtos.get(0).getSourceAmount()).isEqualTo(AMOUNT);
		assertThat(conversionHistoryDtos.get(0).getSourceCurrency()).isEqualTo(CURRENCY_USD);
		assertThat(conversionHistoryDtos.get(0).getTargetAmount()).isEqualTo(TARGET_CURRENCY);
		assertThat(conversionHistoryDtos.get(0).getTargetCurrency()).isEqualTo(CURRENCY_TRY);
	}

	@Test
	public void getConversionByTransactionId_ShouldInvokeRepositoryOnce_WhenConversionFound() {

		Conversion conversion = happyPathConversionRepositoryFindByIdMock();
		happyPathMapperMock(conversion);

		service.getConversionByTransactionId(TRANSACTION_ID);

		verify(conversionRepository, times(1)).findById(TRANSACTION_ID);
	}

	@Test
	public void getConversionByTransactionId_ShouldInvokeMapperOnce_WhenConversionFound() {

		Conversion conversion = happyPathConversionRepositoryFindByIdMock();
		happyPathMapperMock(conversion);

		service.getConversionByTransactionId(TRANSACTION_ID);

		verify(mapper, times(1)).entityToConversionHistoryDTO(conversion);
	}

	@Test
	public void getConversionByTransactionId_ShouldThrowConversionNotFoundException_WhenConversionNotFound() {

		Conversion conversion = new Conversion(CURRENCY_USD, AMOUNT, CURRENCY_TRY, TARGET_CURRENCY, EXCHANGE_RATE);
		conversion.setId(TRANSACTION_ID);

		when(conversionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.empty());

		Assertions.assertThrows(ConversionNotFoundException.class, () -> {

			service.getConversionByTransactionId(TRANSACTION_ID);
		});
	}

	@Test
	public void getConversionByTransactionId_ShouldNotInvokeMapper_WhenConversionNotFound() {

		Conversion conversion = new Conversion(CURRENCY_USD, AMOUNT, CURRENCY_TRY, TARGET_CURRENCY, EXCHANGE_RATE);
		conversion.setId(TRANSACTION_ID);

		when(conversionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.empty());

		try {
			service.getConversionByTransactionId(TRANSACTION_ID);
		} catch (Exception e) {
		}

		verify(mapper, times(0)).entityToConversionHistoryDTO(any(Conversion.class));
	}

	@Test
	public void getConversionByTransactionId_ShouldNotInvokeMapper_WhenExceptionThrownByRepository() {
		
		when(conversionRepository.findById(TRANSACTION_ID)).thenThrow(new RuntimeException());
		
		try {
			service.getConversionByTransactionId(TRANSACTION_ID);
		} catch (Exception e) {
		}
		
		verify(mapper, times(0)).entityToConversionHistoryDTO(any(Conversion.class));
	}

	@Test
	public void getConversionByTransactionDate_ShouldReturnList() {

		Conversion conversion = new Conversion(CURRENCY_USD, AMOUNT, CURRENCY_TRY, TARGET_CURRENCY, EXCHANGE_RATE);
		conversion.setId(TRANSACTION_ID);

		when(conversionRepository.findAllByTransactionTimeGreaterThanEqualAndTransactionTimeLessThanEqual(
				any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
				.thenReturn(List.of(conversion));

		String conversionTime = happyPathMapperMock(conversion);

		List<ConversionHistoryDTO> conversionHistoryDtos = service.getConversionByTransactionDate(TRANSACTION_DATE, 0,
				10);

		assertThat(conversionHistoryDtos).isNotNull();
		assertThat(conversionHistoryDtos.size()).isEqualTo(1);
		assertThat(conversionHistoryDtos.get(0)).isNotNull();
		assertThat(conversionHistoryDtos.get(0).getTransactionId()).isEqualTo(TRANSACTION_ID);
		assertThat(conversionHistoryDtos.get(0).getTransactionTime()).isEqualTo(conversionTime);
		assertThat(conversionHistoryDtos.get(0).getSourceAmount()).isEqualTo(AMOUNT);
		assertThat(conversionHistoryDtos.get(0).getSourceCurrency()).isEqualTo(CURRENCY_USD);
		assertThat(conversionHistoryDtos.get(0).getTargetAmount()).isEqualTo(TARGET_CURRENCY);
		assertThat(conversionHistoryDtos.get(0).getTargetCurrency()).isEqualTo(CURRENCY_TRY);
	}

	@Test
	public void getConversionByTransactionDate_ShouldInvokeRepositoryOnce_WhenConversionFound() {

		Conversion conversion = new Conversion(CURRENCY_USD, AMOUNT, CURRENCY_TRY, TARGET_CURRENCY, EXCHANGE_RATE);
		conversion.setId(TRANSACTION_ID);

		when(conversionRepository.findAllByTransactionTimeGreaterThanEqualAndTransactionTimeLessThanEqual(
				any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
				.thenReturn(List.of(conversion));

		happyPathMapperMock(conversion);

		service.getConversionByTransactionDate(TRANSACTION_DATE, 0, 10);

		verify(conversionRepository, times(1)).findAllByTransactionTimeGreaterThanEqualAndTransactionTimeLessThanEqual(
				any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
	}

	@Test
	public void getConversionByTransactionDate_ShouldInvokeMapperOnce_WhenConversionFound() {

		Conversion conversion = new Conversion(CURRENCY_USD, AMOUNT, CURRENCY_TRY, TARGET_CURRENCY, EXCHANGE_RATE);
		conversion.setId(TRANSACTION_ID);

		when(conversionRepository.findAllByTransactionTimeGreaterThanEqualAndTransactionTimeLessThanEqual(
				any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
				.thenReturn(List.of(conversion));

		happyPathMapperMock(conversion);

		service.getConversionByTransactionDate(TRANSACTION_DATE, 0, 10);

		verify(mapper, times(1)).entityToConversionHistoryDTO(conversion);
	}

	@Test
	public void getConversionByTransactionDate_ShouldThrowConversionNotFoundException_WhenConversionNotFound() {

		Conversion conversion = new Conversion(CURRENCY_USD, AMOUNT, CURRENCY_TRY, TARGET_CURRENCY, EXCHANGE_RATE);
		conversion.setId(TRANSACTION_ID);

		when(conversionRepository.findAllByTransactionTimeGreaterThanEqualAndTransactionTimeLessThanEqual(
				any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of());

		Assertions.assertThrows(ConversionNotFoundException.class, () -> {

			service.getConversionByTransactionDate(TRANSACTION_DATE, 0, 10);
		});
	}

	@Test
	public void getConversionByTransactionDate_ShouldNotInvokeMapper_WhenConversionNotFound() {

		Conversion conversion = new Conversion(CURRENCY_USD, AMOUNT, CURRENCY_TRY, TARGET_CURRENCY, EXCHANGE_RATE);
		conversion.setId(TRANSACTION_ID);

		when(conversionRepository.findAllByTransactionTimeGreaterThanEqualAndTransactionTimeLessThanEqual(
				any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of());

		try {
			service.getConversionByTransactionDate(TRANSACTION_DATE, 0, 10);
		} catch (Exception e) {
		}

		verify(mapper, times(0)).entityToConversionHistoryDTO(any(Conversion.class));
	}

	@Test
	public void getConversionByTransactionDate_ShouldNotInvokeMapper_WhenExceptionThrownByRepository() {
		
		when(conversionRepository.findAllByTransactionTimeGreaterThanEqualAndTransactionTimeLessThanEqual(
				any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenThrow(new RuntimeException());
		
		try {
			service.getConversionByTransactionDate(TRANSACTION_DATE, 0, 10);
		} catch (Exception e) {
		}
		
		verify(mapper, times(0)).entityToConversionHistoryDTO(any(Conversion.class));
	}

	private void happyPathExchangeRateServiceMock() {
		
		when(exchangeRateService.getExchangeRate(CURRENCY_TRY, CURRENCY_USD)).thenReturn(EXCHANGE_RATE);
	}

	private void happyPathConversionRepositorySaveMock() {

		Conversion conversion = new Conversion(CURRENCY_USD, AMOUNT, CURRENCY_TRY, TARGET_CURRENCY, EXCHANGE_RATE);
		conversion.setId(TRANSACTION_ID);

		when(conversionRepository.save(any(Conversion.class))).thenReturn(conversion);
	}

	private Conversion happyPathConversionRepositoryFindByIdMock() {

		Conversion conversion = new Conversion(CURRENCY_USD, AMOUNT, CURRENCY_TRY, TARGET_CURRENCY, EXCHANGE_RATE);
		conversion.setId(TRANSACTION_ID);

		when(conversionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(conversion));

		return conversion;
	}

	private String happyPathMapperMock(Conversion conversion) {

		ConversionHistoryDTO conversionHistoryDto = new ConversionHistoryDTO();
		conversionHistoryDto.setTransactionId(TRANSACTION_ID);
		conversionHistoryDto.setSourceAmount(AMOUNT);
		conversionHistoryDto.setSourceCurrency(CURRENCY_USD);
		conversionHistoryDto.setTargetAmount(TARGET_CURRENCY);
		conversionHistoryDto.setTargetCurrency(CURRENCY_TRY);
		String conversionTime = DateHelper.localDateTimeToFormattedStr(DateHelper.getNowAsDateTime());
		conversionHistoryDto.setTransactionTime(conversionTime);

		when(mapper.entityToConversionHistoryDTO(conversion)).thenReturn(conversionHistoryDto);

		return conversionTime;
	}

	private static final Long TRANSACTION_ID = 5L;
	private static final String TRANSACTION_DATE = "2023-12-28";
	private static final BigDecimal AMOUNT = new BigDecimal(20.52);
	private static final BigDecimal EXCHANGE_RATE = new BigDecimal(0.000001);
	private static final BigDecimal TARGET_CURRENCY = EXCHANGE_RATE.multiply(AMOUNT);
	private static final String CURRENCY_TRY = "TRY";
	private static final String CURRENCY_USD = "USD";
}