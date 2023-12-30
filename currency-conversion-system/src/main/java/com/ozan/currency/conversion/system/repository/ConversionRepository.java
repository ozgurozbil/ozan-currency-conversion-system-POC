package com.ozan.currency.conversion.system.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ozan.currency.conversion.system.model.Conversion;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {

	List<Conversion> findAllByTransactionTimeGreaterThanEqualAndTransactionTimeLessThanEqual(LocalDateTime begin,
			LocalDateTime end, Pageable pageable);
}