package com.ozan.currency.conversion.system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ozan.currency.conversion.system.dto.ConversionDTO;
import com.ozan.currency.conversion.system.dto.ConversionHistoryDTO;
import com.ozan.currency.conversion.system.model.Conversion;

@Mapper(componentModel = "spring")
public interface BeanMapper {

	@Mapping(target = "transactionId", source = "conversion.id")
	@Mapping(target = "transactionTime", expression = "java(com.ozan.currency.conversion.system.util.helper.DateHelper.localDateTimeToFormattedStr(conversion.getTransactionTime()))")
	ConversionHistoryDTO entityToConversionHistoryDTO(Conversion conversion);

	@Mapping(target = "transactionId", source = "conversion.id")
	ConversionDTO entityToConversionDTO(Conversion conversion);
}