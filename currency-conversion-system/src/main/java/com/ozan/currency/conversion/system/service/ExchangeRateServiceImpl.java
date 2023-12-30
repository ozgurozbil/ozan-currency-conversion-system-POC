package com.ozan.currency.conversion.system.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ozan.currency.conversion.system.dto.provider.FixerLastestRatesResponse;
import com.ozan.currency.conversion.system.exception.CurrencyNotFoundException;
import com.ozan.currency.conversion.system.model.ExchangeRate;
import com.ozan.currency.conversion.system.repository.ExchangeRateRepository;
import com.ozan.currency.conversion.system.util.helper.DateHelper;
import com.ozan.currency.conversion.system.util.type.ErrorType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

	private final ConversionProviderIntegrationService conversionProviderService;
	private final ExchangeRateRepository exchangeRateRepository;

	private static final String BASE_CURRENCY = "EUR";

	@Override
	@Transactional
	public BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency) {

		checkAndCreateDailyRates();

		Optional<ExchangeRate> exhangeRate = exchangeRateRepository.findBySourceCurrencyAndTargetCurrencyAndDate(
				sourceCurrency, targetCurrency, DateHelper.getNowAsDate());
		if (exhangeRate.isPresent()) {

			return exhangeRate.get().getRate();
		} else {

			return calculateAndSaveTransitionalRates(sourceCurrency, targetCurrency);
		}
	}

	@Override
	public void checkAndCreateDailyRates() {

		Boolean dailyRatesExist = exchangeRateRepository.existsByDate(DateHelper.getNowAsDate());
		if (!dailyRatesExist) {
			getDailyExchangeRatesOverEURFromProvider();
		}
	}

	@Override
	public BigDecimal calculateAndSaveTransitionalRates(String sourceCurrency, String targetCurrency) {

		ExchangeRate exhangeRateSourceToEUR = getLeftTransitionalExchangeRate(sourceCurrency);
		ExchangeRate exhangeRateEURToTarget = getRightTransitionalExchangeRate(targetCurrency);
		BigDecimal transitionalExchangeRate = exhangeRateSourceToEUR.getRate()
				.multiply(exhangeRateEURToTarget.getRate()).setScale(6, RoundingMode.CEILING);

		saveExchangeRateWithReverse(sourceCurrency, targetCurrency, transitionalExchangeRate,
				DateHelper.getNowAsDate());

		return transitionalExchangeRate;
	}

	private void getDailyExchangeRatesOverEURFromProvider() {

		FixerLastestRatesResponse response = conversionProviderService.getLastestExchangeRate();

		response.getRates().forEach((targetCurrency, rate) -> {
			saveExchangeRateWithReverse(response.getBase(), targetCurrency, rate,
					DateHelper.stringToLocalDate(response.getDate()));
		});
	}

	private ExchangeRate getLeftTransitionalExchangeRate(String sourceCurrency) {

		Optional<ExchangeRate> sourceToEUR = exchangeRateRepository
				.findBySourceCurrencyAndTargetCurrencyAndDate(sourceCurrency, BASE_CURRENCY, DateHelper.getNowAsDate());

		ExchangeRate exhangeRateSourceToEUR = sourceToEUR.orElseThrow(
				() -> new CurrencyNotFoundException(ErrorType.CURRENCY_NOT_FOUND_EXCEPTION, sourceCurrency));

		return exhangeRateSourceToEUR;
	}

	private ExchangeRate getRightTransitionalExchangeRate(String targetCurrency) {

		Optional<ExchangeRate> eurToTarget = exchangeRateRepository
				.findBySourceCurrencyAndTargetCurrencyAndDate(BASE_CURRENCY, targetCurrency, DateHelper.getNowAsDate());

		ExchangeRate exhangeRateEURToTarget = eurToTarget.orElseThrow(
				() -> new CurrencyNotFoundException(ErrorType.CURRENCY_NOT_FOUND_EXCEPTION, targetCurrency));

		return exhangeRateEURToTarget;
	}

	private void saveExchangeRateWithReverse(String sourceCurrency, String targetCurrency, BigDecimal rate,
			LocalDate date) {

		ExchangeRate exchangeRate = new ExchangeRate(sourceCurrency, targetCurrency, rate, date);
		ExchangeRate exchangeRateReverse = new ExchangeRate(targetCurrency, sourceCurrency,
				new BigDecimal(1.0).divide(rate, 6, RoundingMode.CEILING), date);

		exchangeRateRepository.saveAll(List.of(exchangeRate, exchangeRateReverse));
	}
}