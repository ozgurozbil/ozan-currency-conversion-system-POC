package com.ozan.currency.conversion.system.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ozan.currency.conversion.system.dto.ConversionDTO;
import com.ozan.currency.conversion.system.dto.ConversionHistoryDTO;
import com.ozan.currency.conversion.system.exception.ConversionNotFoundException;
import com.ozan.currency.conversion.system.mapper.BeanMapper;
import com.ozan.currency.conversion.system.model.Conversion;
import com.ozan.currency.conversion.system.repository.ConversionRepository;
import com.ozan.currency.conversion.system.util.helper.DateHelper;
import com.ozan.currency.conversion.system.util.type.ErrorType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConversionServiceImpl implements ConversionService {

	private final ExchangeRateService exchangeRateService;
	private final ConversionRepository conversionRepository;
	private final BeanMapper mapper;

	@Override
	public ConversionDTO convert(BigDecimal amount, String sourceCurrency, String targetCurrency) {

		BigDecimal rate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
		BigDecimal targetAmount = rate.multiply(amount);

		Conversion conversion = conversionRepository
				.save(new Conversion(sourceCurrency, amount, targetCurrency, targetAmount, rate));

		return mapper.entityToConversionDTO(conversion);
	}

	@Override
	public List<ConversionHistoryDTO> getConversionByTransactionId(Long transactionId) {

		Optional<Conversion> result = conversionRepository.findById(transactionId);

		Conversion conversion = result
				.orElseThrow(() -> new ConversionNotFoundException(ErrorType.TRANSACTION_NOT_FOUND, transactionId));

		return List.of(mapper.entityToConversionHistoryDTO(conversion));
	}

	@Override
	public List<ConversionHistoryDTO> getConversionByTransactionDate(String transactionDate, Integer pageNumber,
			Integer pageSize) {

		List<Conversion> conversions = getConversations(transactionDate, pageNumber, pageSize);

		return conversions.stream().map(conversion -> mapper.entityToConversionHistoryDTO(conversion)).toList();
	}

	private List<Conversion> getConversations(String transactionDate, Integer pageNumber, Integer pageSize) {

		LocalDate date = DateHelper.stringToLocalDate(transactionDate);
		LocalDateTime begin = date.atStartOfDay();
		LocalDateTime end = date.atTime(LocalTime.MAX);

		Pageable sortByTransactionTime = PageRequest.of(pageNumber, pageSize, Sort.by("transactionTime").ascending());

		List<Conversion> conversions = conversionRepository
				.findAllByTransactionTimeGreaterThanEqualAndTransactionTimeLessThanEqual(begin, end,
						sortByTransactionTime);

		if (CollectionUtils.isEmpty(conversions)) {
			throw new ConversionNotFoundException(ErrorType.TRANSACTION_NOT_FOUND, transactionDate,
					pageNumber.toString(), pageSize.toString());
		}

		return conversions;
	}
}