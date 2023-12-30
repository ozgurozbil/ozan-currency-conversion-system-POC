package com.ozan.currency.conversion.system.service;

import java.math.BigDecimal;

public interface ExchangeRateService {

	BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency);

	void checkAndCreateDailyRates();

	BigDecimal calculateAndSaveTransitionalRates(String sourceCurrency, String targetCurrency);
}