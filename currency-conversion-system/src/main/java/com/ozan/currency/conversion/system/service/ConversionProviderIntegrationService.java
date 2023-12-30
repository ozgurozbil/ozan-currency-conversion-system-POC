package com.ozan.currency.conversion.system.service;

import com.ozan.currency.conversion.system.dto.provider.FixerLastestRatesResponse;

public interface ConversionProviderIntegrationService {
	
	FixerLastestRatesResponse getLastestExchangeRate();
}