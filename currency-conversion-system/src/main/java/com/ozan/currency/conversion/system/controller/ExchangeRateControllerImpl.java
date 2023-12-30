package com.ozan.currency.conversion.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ozan.currency.conversion.system.controller.api.ExchangeRateController;
import com.ozan.currency.conversion.system.dto.response.ExchangeRateResponse;
import com.ozan.currency.conversion.system.service.ExchangeRateService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/exhange-rate")
public class ExchangeRateControllerImpl implements ExchangeRateController {

	private final ExchangeRateService exchangeRateService;

	@Override
	@GetMapping("/get-rate-between/{sourceCurrency}/{targetCurrency}")
	public ExchangeRateResponse getExchangeRate(@PathVariable("sourceCurrency") String sourceCurrency,
			@PathVariable("targetCurrency") String targetCurrency) {

		return new ExchangeRateResponse().ok().setResult(
				exchangeRateService.getExchangeRate(sourceCurrency.toUpperCase(), targetCurrency.toUpperCase()));
	}
}