package com.satish.currency.converter.util;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.satish.currency.converter.exception.CurrencyConverterException;
import com.satish.currency.converter.exception.CurrencyConverterInternalException;

@Component
public class CurrencyConverterHandleException {

	public Throwable mapWebClientException(WebClientResponseException exception) {
		return new CurrencyConverterInternalException("WebClient Error", exception);
	}

	public Throwable mapWebClientException(WebClientRequestException exception) {
		return new CurrencyConverterInternalException("WebClient Error", exception);
	}
	
	public Throwable baseNotFoundException() {
		return new CurrencyConverterException(Codes.BASE_CODE_NOT_FOUND.getCode(),
				Codes.BASE_CODE_NOT_FOUND.getDescription());
	}
}
