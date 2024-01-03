package com.satish.currency.converter.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.satish.currency.converter.model.ExchangeRateCCResponse;
import com.satish.currency.converter.model.SupportedSymbolsCCResponse;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExchangeRateConnectorTest {
	@Mock
	private WebClient webClientMock;

	@Mock
	private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;

	@Mock
	private WebClient.RequestBodySpec requestBodySpecMock;

	@SuppressWarnings("rawtypes")
	@Mock
	private WebClient.RequestHeadersSpec requestHeadersSpecMock;

	@SuppressWarnings("rawtypes")
	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

	@Mock
	private WebClient.ResponseSpec responseSpecMock;

	@InjectMocks
	private ExchangeRateConnector exchangeRateConnector;

	
	@SuppressWarnings("unchecked")
	@BeforeEach
	void init() {
		 when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
	        when(requestHeadersUriSpecMock.uri(Mockito.any(Function.class)))
	        .thenReturn(requestHeadersSpecMock);
			when(requestHeadersSpecMock.retrieve())
			        .thenReturn(responseSpecMock);
	}

	@Test
	void getExchangeRates() {
		String base = "EUR";
		ExchangeRateCCResponse mockExchangeRateCCResponse = getExchangeRateCCResponse(base, true);

		when(responseSpecMock.bodyToMono(ExchangeRateCCResponse.class))
				.thenReturn(Mono.just(mockExchangeRateCCResponse));

		Mono<ExchangeRateCCResponse> exchangeRateCCResponse = exchangeRateConnector.getExchangeRates(base);
		StepVerifier.create(exchangeRateCCResponse).assertNext(response -> {
			assertTrue(response.isSuccess());
			assertEquals(base, response.getBase());
		}).verifyComplete();
	}

	@Test
	void getExchangeRates_Failure() {
		String base = "EUR";
		ExchangeRateCCResponse mockExchangeRateCCResponse = getExchangeRateCCResponse(base, false);

		when(responseSpecMock.bodyToMono(ExchangeRateCCResponse.class))
				.thenReturn(Mono.just(mockExchangeRateCCResponse));

		Mono<ExchangeRateCCResponse> exchangeRateCCResponse = exchangeRateConnector.getExchangeRates(base);
		StepVerifier.create(exchangeRateCCResponse).expectNextMatches(response -> {
			System.out.println(response.isSuccess());
			return !response.isSuccess();
		}).verifyComplete();
	}

	
	@Test
	void getSymbols() {
		SupportedSymbolsCCResponse mockSupportedSymbolsCCResponse = getSupportedSymbolsCCResponse(true);

		when(responseSpecMock.bodyToMono(SupportedSymbolsCCResponse.class))
				.thenReturn(Mono.just(mockSupportedSymbolsCCResponse));

		Mono<SupportedSymbolsCCResponse> exchangeRateCCResponse = exchangeRateConnector.getSymbols();
		StepVerifier.create(exchangeRateCCResponse).expectNextMatches(response -> response.isSuccess())
				.verifyComplete();
	}

	@Test
	void getSymbols_Failure() {
		SupportedSymbolsCCResponse mockSupportedSymbolsCCResponse = getSupportedSymbolsCCResponse(false);

		when(responseSpecMock.bodyToMono(SupportedSymbolsCCResponse.class))
				.thenReturn(Mono.just(mockSupportedSymbolsCCResponse));

		Mono<SupportedSymbolsCCResponse> exchangeRateCCResponse = exchangeRateConnector.getSymbols();
		StepVerifier.create(exchangeRateCCResponse).expectNextMatches(response -> !response.isSuccess())
				.verifyComplete();
	}
	

	private ExchangeRateCCResponse getExchangeRateCCResponse(String base, boolean status) {
		Map<String, Double> rates = new HashMap<>();
		rates.put("AED", 4.003077);
		rates.put("AFN", 75.81361);
		ExchangeRateCCResponse mockExchangeRateCCResponse = new ExchangeRateCCResponse();
		mockExchangeRateCCResponse.setSuccess(status);
		mockExchangeRateCCResponse.setTimestamp(1701403983);
		mockExchangeRateCCResponse.setBase(base);
		mockExchangeRateCCResponse.setDate("2023-12-11");
		mockExchangeRateCCResponse.setRates(rates);
		return mockExchangeRateCCResponse;
	}

	private SupportedSymbolsCCResponse getSupportedSymbolsCCResponse(boolean status) {
		Map<String, String> symbols = new HashMap<>();
		symbols.put("AED", "AED");
		symbols.put("AFN", "AEN");
		SupportedSymbolsCCResponse mockSupportedSymbolsCCResponse = new SupportedSymbolsCCResponse();
		mockSupportedSymbolsCCResponse.setSuccess(status);
		mockSupportedSymbolsCCResponse.setSymbols(symbols);
		return mockSupportedSymbolsCCResponse;
	}


}