package com.ozan.currency.conversion.system.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ozan.currency.conversion.system.config.ConversionProviderProperties;
import com.ozan.currency.conversion.system.dto.provider.FixerLastestRatesResponse;
import com.ozan.currency.conversion.system.exception.ConversionProviderException;
import com.ozan.currency.conversion.system.util.type.ErrorType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConversionProviderIntegrationServiceImpl implements ConversionProviderIntegrationService {

	private final ConversionProviderProperties providerProperties;
	private final RestTemplate restTemplate;

	@Override
	public FixerLastestRatesResponse getLastestExchangeRate() {

		FixerLastestRatesResponse response = null;

		int callTryCount = 1;
		while (callTryCount <= 3) {
			callTryCount++;
			try {
				response = restTemplate.getForObject(providerProperties.getLastestRatesEndpoint(),
						FixerLastestRatesResponse.class);
			} catch (Exception e) {

				if (callTryCount <= 3) {

					threadSleep();
					continue;
				} else {

					throw new ConversionProviderException(ErrorType.CONVERSION_PROVIDER_ERROR, callTryCount);
				}
			}

			if (Objects.nonNull(response) && Objects.nonNull(response.getError())) {
				if (callTryCount <= 3) {

					threadSleep();
					continue;
				} else {

					throw new ConversionProviderException(ErrorType.CONVERSION_PROVIDER_ERROR,
							response.getError().getCode() + "-" + response.getError().getInfo(), callTryCount);
				}
			}
		}

		return response;
	}

	private void threadSleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
		}
	}
}