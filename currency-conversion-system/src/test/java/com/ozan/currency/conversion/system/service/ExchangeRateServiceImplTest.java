package com.ozan.currency.conversion.system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ozan.currency.conversion.system.dto.provider.FixerLastestRatesResponse;
import com.ozan.currency.conversion.system.dto.provider.FixerResponseError;
import com.ozan.currency.conversion.system.exception.ConversionProviderException;
import com.ozan.currency.conversion.system.exception.CurrencyNotFoundException;
import com.ozan.currency.conversion.system.model.ExchangeRate;
import com.ozan.currency.conversion.system.repository.ExchangeRateRepository;
import com.ozan.currency.conversion.system.util.type.ErrorType;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceImplTest {

	@InjectMocks
	private ExchangeRateServiceImpl service;
	@Mock
	private ConversionProviderIntegrationService conversionProviderService;
	@Mock
	private ExchangeRateRepository repository;

	@Test
	public void getExchangeRate_shouldReturnRate_whenDailyRateIsFoundInDB() {

		checkAndCreateDailyRatesHappyPathMock();

		Optional<ExchangeRate> exchangeRate = Optional
				.of(new ExchangeRate(CURRENCY_USD, CURRENCY_TRY, EXCHANGE_RATE, DATE_NOW));

		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(anyString(), anyString(), any(LocalDate.class)))
				.thenReturn(exchangeRate);

		BigDecimal rate = service.getExchangeRate(CURRENCY_USD, CURRENCY_TRY);

		assertThat(rate).isEqualTo(EXCHANGE_RATE);
	}

	@Test
	public void getExchangeRate_shouldInvokeRepositoryAtMostOnce_whenDailyRateIsFoundInDB() {

		checkAndCreateDailyRatesHappyPathMock();

		Optional<ExchangeRate> exchangeRate = Optional
				.of(new ExchangeRate(CURRENCY_USD, CURRENCY_TRY, EXCHANGE_RATE, DATE_NOW));

		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(anyString(), anyString(), any(LocalDate.class)))
				.thenReturn(exchangeRate);

		service.getExchangeRate(CURRENCY_USD, CURRENCY_TRY);

		verify(repository, atMostOnce()).findBySourceCurrencyAndTargetCurrencyAndDate(anyString(), anyString(),
				any(LocalDate.class));
	}

	@Test
	public void checkAndCreateDailyRates_shouldInvokeProviderOnce_whenDailyRatesAreNotFound() {

		ProviderHappyPathMock();

		service.checkAndCreateDailyRates();

		verify(conversionProviderService, times(1)).getLastestExchangeRate();
	}

	@Test
	public void checkAndCreateDailyRates_shouldInvokeRepositoryAtLeastOnce_whenProviderReturnsRates() {

		checkAndCreateDailyRatesHappyPathMock();

		service.checkAndCreateDailyRates();

		verify(conversionProviderService, atLeast(1)).getLastestExchangeRate();
	}

	@Test
	public void checkAndCreateDailyRates_shouldNotInvokeProvider_whenDailyRatesAreFound() {

		when(repository.existsByDate(DATE_NOW)).thenReturn(true);
		
		service.checkAndCreateDailyRates();
		
		verify(conversionProviderService, times(0)).getLastestExchangeRate();
	}

	@Test
	public void checkAndCreateDailyRates_shouldNotInvokeRepository_WhenProviderReturnsError() {

		FixerLastestRatesResponse response = new FixerLastestRatesResponse();
		FixerResponseError error = new FixerResponseError();
		error.setCode(105);
		error.setInfo("message");
		response.setError(error);

		when(conversionProviderService.getLastestExchangeRate())
				.thenThrow(new ConversionProviderException(ErrorType.CONVERSION_PROVIDER_ERROR, 1));

		Assertions.assertThrows(ConversionProviderException.class, () -> {

			service.checkAndCreateDailyRates();
		});

		verify(repository, times(0)).saveAll(any(List.class));
	}

	@Test
	public void calculateAndSaveTransitionalRates_shouldReturnExchangeRate_whenHappyPathScenarioHappens() {

		repositoryTransitionalFindByHappyPathMock();

		repositorySaveAllHappyPathMock();

		BigDecimal rate = service.calculateAndSaveTransitionalRates(CURRENCY_USD, CURRENCY_TRY);

		assertThat(rate).isEqualTo(EXCHANGE_RATE_LEFT.multiply(EXCHANGE_RATE_RIGHT).setScale(6, RoundingMode.CEILING));
	}

	@Test
	public void calculateAndSaveTransitionalRates_shouldInvokeRepositorySaveAllOnce_whenSourceAndTargetCurrenciesAreFound() {

		Optional<ExchangeRate> exchangeRate = Optional
				.of(new ExchangeRate(CURRENCY_USD, CURRENCY_TRY, EXCHANGE_RATE, DATE_NOW));

		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(anyString(), anyString(), any(LocalDate.class)))
				.thenReturn(exchangeRate);
		repositorySaveAllHappyPathMock();

		service.calculateAndSaveTransitionalRates(CURRENCY_USD, CURRENCY_TRY);

		verify(repository, times(1)).saveAll(any(List.class));
	}

	@Test
	public void calculateAndSaveTransitionalRates_shouldInvokeRepositoryFindTargetRate_whenSourceCurrencyIsFound() {

		repositoryTransitionalFindByHappyPathMock();

		repositorySaveAllHappyPathMock();

		service.calculateAndSaveTransitionalRates(CURRENCY_USD, CURRENCY_TRY);

		verify(repository, times(1)).findBySourceCurrencyAndTargetCurrencyAndDate(BASE_CURRENCY, CURRENCY_TRY,
				DATE_NOW);
	}

	@Test
	public void calculateAndSaveTransitionalRates_shouldThrowCurrencyNotFoundException_whenSourceCurrencyIsNotFound() {

		Optional<ExchangeRate> exchangeRate = Optional.empty();

		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(CURRENCY_USD, BASE_CURRENCY, DATE_NOW))
				.thenReturn(exchangeRate);

		Assertions.assertThrows(CurrencyNotFoundException.class, () -> {

			service.calculateAndSaveTransitionalRates(CURRENCY_USD, CURRENCY_TRY);
		});
	}

	@Test
	public void calculateAndSaveTransitionalRates_shouldNotInvokeRepositorySaveAll_whenSourceCurrencyIsNotFound() {

		Optional<ExchangeRate> exchangeRate = Optional.empty();

		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(CURRENCY_USD, BASE_CURRENCY, DATE_NOW))
				.thenReturn(exchangeRate);

		try {
			service.calculateAndSaveTransitionalRates(CURRENCY_USD, CURRENCY_TRY);
		} catch (Exception e) {
		}

		verify(repository, times(0)).saveAll(any(List.class));
	}

	@Test
	public void calculateAndSaveTransitionalRates_shouldThrowCurrencyNotFoundException_whenTargetCurrencyIsNotFound() {

		Optional<ExchangeRate> sourceExchangeRate = Optional
				.of(new ExchangeRate(CURRENCY_USD, CURRENCY_TRY, EXCHANGE_RATE_LEFT, DATE_NOW));
		Optional<ExchangeRate> targetExchangeRate = Optional.empty();

		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(CURRENCY_USD, BASE_CURRENCY, DATE_NOW))
				.thenReturn(sourceExchangeRate);
		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(BASE_CURRENCY, CURRENCY_TRY, DATE_NOW))
				.thenReturn(targetExchangeRate);

		Assertions.assertThrows(CurrencyNotFoundException.class, () -> {

			service.calculateAndSaveTransitionalRates(CURRENCY_USD, CURRENCY_TRY);
		});
	}

	@Test
	public void calculateAndSaveTransitionalRates_shouldNotInvokeRepositorySaveAll_whenTargetCurrencyIsNotFound() {

		Optional<ExchangeRate> sourceExchangeRate = Optional
				.of(new ExchangeRate(CURRENCY_USD, CURRENCY_TRY, EXCHANGE_RATE_LEFT, DATE_NOW));
		Optional<ExchangeRate> targetExchangeRate = Optional.empty();

		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(CURRENCY_USD, BASE_CURRENCY, DATE_NOW))
				.thenReturn(sourceExchangeRate);
		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(BASE_CURRENCY, CURRENCY_TRY, DATE_NOW))
				.thenReturn(targetExchangeRate);

		try {
			service.calculateAndSaveTransitionalRates(CURRENCY_USD, CURRENCY_TRY);
		} catch (Exception e) {
		}

		verify(repository, times(0)).saveAll(any(List.class));
	}

	private void checkAndCreateDailyRatesHappyPathMock() {
		ProviderHappyPathMock();
		repositorySaveAllHappyPathMock();
	}

	private void ProviderHappyPathMock() {
		
		when(repository.existsByDate(DATE_NOW)).thenReturn(false);
		
		FixerLastestRatesResponse response = new FixerLastestRatesResponse();
		response.setRates(Map.of(CURRENCY_TRY, EXCHANGE_RATE_LEFT, CURRENCY_USD, EXCHANGE_RATE_RIGHT));
		response.setDate(DATE_NOW.toString());
		when(conversionProviderService.getLastestExchangeRate()).thenReturn(response);
	}

	private void repositorySaveAllHappyPathMock() {
		
		when(repository.saveAll(any(List.class))).thenReturn(new ArrayList<>());
	}

	private void repositoryTransitionalFindByHappyPathMock() {

		Optional<ExchangeRate> sourceExchangeRate = Optional
				.of(new ExchangeRate(CURRENCY_USD, CURRENCY_TRY, EXCHANGE_RATE_LEFT, DATE_NOW));
		Optional<ExchangeRate> targetExchangeRate = Optional
				.of(new ExchangeRate(CURRENCY_TRY, CURRENCY_USD, EXCHANGE_RATE_RIGHT, DATE_NOW));

		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(CURRENCY_USD, BASE_CURRENCY, DATE_NOW))
				.thenReturn(sourceExchangeRate);
		when(repository.findBySourceCurrencyAndTargetCurrencyAndDate(BASE_CURRENCY, CURRENCY_TRY, DATE_NOW))
				.thenReturn(targetExchangeRate);
	}

	private static final LocalDate DATE_NOW = LocalDate.now();
	private static final BigDecimal EXCHANGE_RATE = new BigDecimal(10.01);
	private static final BigDecimal EXCHANGE_RATE_LEFT = new BigDecimal(0.5);
	private static final BigDecimal EXCHANGE_RATE_RIGHT = new BigDecimal(10);
	private static final String BASE_CURRENCY = "EUR";
	private static final String CURRENCY_TRY = "TRY";
	private static final String CURRENCY_USD = "USD";
}