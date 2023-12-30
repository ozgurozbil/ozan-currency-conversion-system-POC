package com.ozan.currency.conversion.system.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "EXCHANGE_RATE")
public class ExchangeRate implements Serializable {

	private static final long serialVersionUID = -4314672184229759286L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "SOURCE_CURRENCY", length = 3, nullable = false)
	private String sourceCurrency;

	@Column(name = "TARGET_CURRENCY", length = 3, nullable = false)
	private String targetCurrency;
	
	@Column(name = "RATE", nullable = false, precision = 17, scale = 6)
	private BigDecimal rate;

	@Column(name = "DATE", nullable = false)
	private LocalDate date;

	public ExchangeRate(String sourceCurrency, String targetCurrency, BigDecimal rate, LocalDate date) {
		super();
		this.sourceCurrency = sourceCurrency;
		this.targetCurrency = targetCurrency;
		this.date = date;
		this.rate = rate;
	}
}