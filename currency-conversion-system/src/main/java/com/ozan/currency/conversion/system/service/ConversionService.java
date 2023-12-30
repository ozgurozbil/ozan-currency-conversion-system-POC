package com.ozan.currency.conversion.system.service;

import java.math.BigDecimal;
import java.util.List;

import com.ozan.currency.conversion.system.dto.ConversionDTO;
import com.ozan.currency.conversion.system.dto.ConversionHistoryDTO;

public interface ConversionService {

	ConversionDTO convert(BigDecimal amount, String sourceCurrency, String targetCurrency);

	List<ConversionHistoryDTO> getConversionByTransactionId(Long transactionId);

	List<ConversionHistoryDTO> getConversionByTransactionDate(String transactionDate, Integer pageNumber,
			Integer pageSize);

}