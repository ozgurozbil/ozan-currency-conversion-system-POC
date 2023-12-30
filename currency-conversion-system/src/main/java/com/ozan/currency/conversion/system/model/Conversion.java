package com.ozan.currency.conversion.system.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.ozan.currency.conversion.system.util.helper.DateHelper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CONVERSION")
public class Conversion implements Serializable {

	private static final long serialVersionUID = 3631372168275955434L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "SOURCE_CURRENCY", length = 3, nullable = false)
	private String sourceCurrency;

	@Column(name = "SOURCE_AMOUNT", nullable = false, precision = 20, scale = 2)
	private BigDecimal sourceAmount;

	@Column(name = "TARGET_CURRENCY", length = 3, nullable = false)
	private String targetCurrency;

	@Column(name = "TARGET_AMOUNT", nullable = false, precision = 20, scale = 2)
	private BigDecimal targetAmount;

	@Column(name = "EXCHANGE_RATE", nullable = false, precision = 17, scale = 6)
	private BigDecimal exchangeRate;

	@Column(name = "TRANSACTION_TIME", nullable = false)
	private LocalDateTime transactionTime;

	public Conversion(String sourceCurrency, BigDecimal sourceAmount, String targetCurrency, BigDecimal targetAmount,
			BigDecimal exchangeRate) {
		super();
		this.sourceCurrency = sourceCurrency;
		this.sourceAmount = sourceAmount.setScale(2, RoundingMode.CEILING);
		this.targetCurrency = targetCurrency;
		this.targetAmount = targetAmount.setScale(2, RoundingMode.CEILING);
		this.exchangeRate = exchangeRate.setScale(6, RoundingMode.CEILING);
		this.transactionTime = DateHelper.getNowAsDateTime();
	}
}