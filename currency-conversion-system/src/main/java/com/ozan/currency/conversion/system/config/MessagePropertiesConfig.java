package com.ozan.currency.conversion.system.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@PropertySource("classpath:messages.properties")
@AllArgsConstructor
public class MessagePropertiesConfig {

	private final Environment environment;

	public String getConfigValue(String configKey) {

		return environment.getProperty(configKey);
	}
}