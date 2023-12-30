package com.ozan.currency.conversion.system.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ozan.currency.conversion.system.controller.api.ConversionController;
import com.ozan.currency.conversion.system.dto.ConversionHistoryDTO;
import com.ozan.currency.conversion.system.dto.response.ConversionHistoryResponse;
import com.ozan.currency.conversion.system.dto.response.ConvertCurrencyResponse;
import com.ozan.currency.conversion.system.exception.ConversionIllegalArgumentException;
import com.ozan.currency.conversion.system.service.ConversionService;
import com.ozan.currency.conversion.system.util.helper.StringHelper;
import com.ozan.currency.conversion.system.util.type.ErrorType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/conversion")
public class ConversionControllerImpl implements ConversionController {

	private final ConversionService conversionService;

	@Override
	@GetMapping("/convert/{amount}/{sourceCurrency}/{targetCurrency}")
	public ConvertCurrencyResponse convertCurrency(@PathVariable("amount") BigDecimal amount,
			@PathVariable(name = "sourceCurrency") String sourceCurrency,
			@PathVariable("targetCurrency") String targetCurrency) {

		return new ConvertCurrencyResponse().ok()
				.setResult(conversionService.convert(amount, sourceCurrency.toUpperCase(), targetCurrency.toUpperCase()));
	}

	@Override
	@GetMapping("/history")
	public ConversionHistoryResponse getHistory(
			@RequestParam(name = "transactionId", required = false) Long transactionId,
			@RequestParam(name = "transactionDate", required = false) String transactionDate,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		final List<ConversionHistoryDTO> histories;
		if (Objects.nonNull(transactionId)) {
			histories = conversionService.getConversionByTransactionId(transactionId);
		} else if (StringHelper.isNotBlank(transactionDate)) {
			histories = conversionService.getConversionByTransactionDate(transactionDate, pageNumber, pageSize);
		} else {
			throw new ConversionIllegalArgumentException(ErrorType.ILLEGAL_ARGUMENT, "no.transaction.identifier");
		}

		return new ConversionHistoryResponse().ok().setResult(histories);
	}
}