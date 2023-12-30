package com.ozan.currency.conversion.system.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ozan.currency.conversion.system.model.ExchangeRate;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

	Optional<ExchangeRate> findBySourceCurrencyAndTargetCurrencyAndDate(String sourceCurrency, String targetCurrency, LocalDate date);
	
	Boolean existsByDate(LocalDate date);
}