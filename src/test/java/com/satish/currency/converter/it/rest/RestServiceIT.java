package com.satish.currency.converter.it.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.satish.currency.converter.CCApplication;
import com.satish.currency.converter.model.request.ConversionRequest;
import com.satish.currency.converter.model.response.ConversionResponse;
import com.satish.currency.converter.model.response.ExchangeRateResponse;
import com.satish.currency.converter.model.response.SupportedSymbolsResponse;
import com.satish.currency.converter.util.Codes;


@SpringBootTest(classes = CCApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location = classpath:application-it.yml"})
@ExtendWith(SpringExtension.class)
class RestServiceIT {

	@LocalServerPort
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	
	HttpHeaders headers = new HttpHeaders();
	
	@Autowired
	private WebTestClient webClient;
	
	
	//NOTE: set EXCHANGE_RATE_ACCESS_KEY in environment variable
	
	@Test
	public void testRetrieveSymbols_webClient() throws JSONException {
		SupportedSymbolsResponse symbols = this.webClient.get().uri("/api/v1/symbols")
													            .exchange()
													            .expectStatus().isOk()
													            .expectBody(SupportedSymbolsResponse.class)
													            .returnResult()
													            .getResponseBody();
		assertEquals(Codes.SUCCESS.getCode(), symbols.getCode());
		assertTrue(symbols.getSymbols().size() > 0, "Number of symbols should be greater than zero");
	}
	
	@Test
	public void testRetrieveSymbols() throws JSONException {
		SupportedSymbolsResponse symbols = setHTTPEntitySymbolsAPIAndReturnSupportedSymbolsResponse();
		
		assertEquals(Codes.SUCCESS.getCode(), symbols.getCode());
		assertTrue(symbols.getSymbols().size() > 0, "Number of symbols should be greater than zero");
	}
	
	@Test
	public void testGetExchangeRates() throws JSONException {
		String base = "EUR";
		ExchangeRateResponse ratesResponse = setHTTPEntityExchangeRatesAPIAndReturnExchangeRateResponse(base);
		
		assertEquals(Codes.SUCCESS.getCode(), ratesResponse.getCode());
		assertEquals(base,ratesResponse.getBase());
		assertTrue(ratesResponse.getRates().size() > 0, "Number of exchange rates should be greater than zero");
	}
	
	@Test
	public void testGetExchangeRates_empty_base() throws JSONException {
		String base = "";
		ExchangeRateResponse ratesResponse = setHTTPEntityExchangeRatesAPIAndReturnExchangeRateResponse(base);
		
		assertEquals(Codes.CONSTRAINT_VIOLATION_ERROR.getCode(), ratesResponse.getCode());
	}
	
	@Test
	public void testConversion() throws JSONException {
		ConversionRequest conversionRequest = new ConversionRequest("EUR","ZMW",3);
		ConversionResponse conversionResponse = setHTTPEnttityConverstionAPIAndReturnConversionResponse(conversionRequest);
		
		assertEquals(Codes.SUCCESS.getCode(), conversionResponse.getCode());
		assertTrue(conversionResponse.getTargetBaseValue() == (double) conversionResponse.getTargetBaseValue() ,"Exchange rate value should be double");
	}
	
	@Test
	public void testConversion_invalid_source_base() throws JSONException {
		ConversionRequest conversionRequest = new ConversionRequest("EUR111","ZMW",3);
		ConversionResponse conversionResponse = setHTTPEnttityConverstionAPIAndReturnConversionResponse(conversionRequest);
		assertEquals(Codes.BASE_CODE_NOT_FOUND.getCode(), conversionResponse.getCode());
	}
	
	@Test
	public void testConversion_invalid_target_base() throws JSONException {
		ConversionRequest conversionRequest = new ConversionRequest("EUR","ZMW1",3);
		ConversionResponse conversionResponse = setHTTPEnttityConverstionAPIAndReturnConversionResponse(conversionRequest);
		assertEquals(Codes.BASE_CODE_NOT_FOUND.getCode(), conversionResponse.getCode());
	}
	
	
	@Test
	public void testConversion_empty_source_base() throws JSONException {
		ConversionRequest conversionRequest = new ConversionRequest("","ZMW1",3);
		ConversionResponse conversionResponse = setHTTPEnttityConverstionAPIAndReturnConversionResponse(conversionRequest);
		assertEquals(Codes.CONSTRAINT_VIOLATION_ERROR.getCode(), conversionResponse.getCode());
	}
	
	@Test
	public void testConversion_empty_target_base() throws JSONException {
		ConversionRequest conversionRequest = new ConversionRequest("EUR","",3);
		ConversionResponse conversionResponse = setHTTPEnttityConverstionAPIAndReturnConversionResponse(conversionRequest);
		assertEquals(Codes.CONSTRAINT_VIOLATION_ERROR.getCode(), conversionResponse.getCode());
	}
	
	@Test
	public void testConversion_negative_source_base_value() throws JSONException {
		ConversionRequest conversionRequest = new ConversionRequest("EUR","ZMW",-1.0);
		ConversionResponse conversionResponse = setHTTPEnttityConverstionAPIAndReturnConversionResponse(conversionRequest);
		assertEquals(Codes.CONSTRAINT_VIOLATION_ERROR.getCode(), conversionResponse.getCode());
	}
	
	private SupportedSymbolsResponse setHTTPEntitySymbolsAPIAndReturnSupportedSymbolsResponse() {
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<SupportedSymbolsResponse> response = restTemplate.exchange(createURLWithPort("/api/v1/symbols"),
				HttpMethod.GET, entity, SupportedSymbolsResponse.class);

		return response.getBody();
	}
	
	private ExchangeRateResponse setHTTPEntityExchangeRatesAPIAndReturnExchangeRateResponse(String base) {
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<ExchangeRateResponse> response = restTemplate.exchange(createURLWithPort("/api/v1/exchangeRates?base="+base),
				HttpMethod.GET, entity, ExchangeRateResponse.class);

		return response.getBody();
	}
	
	private ConversionResponse setHTTPEnttityConverstionAPIAndReturnConversionResponse(ConversionRequest conversionRequest) {
		headers.add("Content-Type", "application/json");
		
		HttpEntity<ConversionRequest> entity = new HttpEntity<>(conversionRequest, headers);
		ResponseEntity<ConversionResponse> response = restTemplate.exchange(createURLWithPort("/api/v1/convert"),
				HttpMethod.POST, entity, ConversionResponse.class);
		
		return response.getBody();
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
