package com.satish.currency.converter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.satish.currency.converter.exception.CurrencyConverterInternalException;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CurrencyConverterHandleExceptionTest {
	
	@Autowired
	CurrencyConverterHandleException currencyConverterHandleException;

	
	@Test
	public void testWebClientResponseException() {
		 WebClientResponseException mockWebClientResponseException = mock(WebClientResponseException.class);
		 Throwable mappedException = currencyConverterHandleException.mapWebClientException(mockWebClientResponseException);
		 assertEquals(CurrencyConverterInternalException.class, mappedException.getClass());
	}
	
	@Test
	public void testWebClientRequestException() {
		WebClientRequestException mockWebClientRequestException = mock(WebClientRequestException.class);
		 Throwable mappedException = currencyConverterHandleException.mapWebClientException(mockWebClientRequestException);
		 assertEquals(CurrencyConverterInternalException.class, mappedException.getClass());
	}
	
}
