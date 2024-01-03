package com.satish.currency.converter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.satish.currency.converter.cache.IExchangeRateCache;
import com.satish.currency.converter.connector.IExchangeRateConnector;
import com.satish.currency.converter.exception.CurrencyConverterInternalException;
import com.satish.currency.converter.model.ExchangeRateCCResponse;
import com.satish.currency.converter.model.SupportedSymbolsCCResponse;
import com.satish.currency.converter.model.response.ExchangeRateResponse;
import com.satish.currency.converter.model.response.SupportedSymbolsResponse;
import com.satish.currency.converter.service.ExchangeRateService;
import com.satish.currency.converter.util.Codes;
import com.satish.currency.converter.util.CurrencyConverterHandleException;
import com.satish.currency.converter.util.CurrencyConverterMapper;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExchangeRateServiceTest  {
	
	@Mock
	IExchangeRateConnector exchangeRateConnector;

	@Mock
	IExchangeRateCache exchangeRateCache;

	@Mock
	CurrencyConverterHandleException currencyConverterHandleException;

	@Mock
	CurrencyConverterMapper currencyConverterMapper;
	
	@Autowired
	CurrencyConverterMapper realCurrencyConverterMapper;
	
	@InjectMocks
	ExchangeRateService exchangeRateService;

	
	
	@Test
	public void getExchangeRates_Success() {
		String base = "EUR";
		setMockExchangeRateCCResponse(base,true,false);
		setMockGetExchangeRateNoDataResponse();
		
		Mono<ExchangeRateResponse> result =  exchangeRateService.getExchangeRates(base);
		
		StepVerifier.create(result)
					.expectNextMatches(t -> {
						 assertEquals(2, t.getRates().size());
						 assertEquals(Codes.SUCCESS.getCode(), t.getCode());
						 assertEquals(base,t.getBase());
						 return true;
					})
					.verifyComplete();
	}
	
	
	@Test
	public void getExchangeRates_No_Data_Error() {
		String base = "EUR";
		setMockExchangeRateCCResponse(base, false, false);
		setMockGetExchangeRateNoDataResponse();
		
		Mono<ExchangeRateResponse> result =  exchangeRateService.getExchangeRates(base);
		
		StepVerifier.create(result)
					.expectNextMatches(t -> t.getCode().equals(Codes.NO_DATA_ERROR.getCode()))
					.verifyComplete();
	}
	
	@Test
	public void getExchangeRates_From_Cache_Success() {
		String base = "EUR";
		setMockExchangeRateCCResponse(base,true,true);
		
		Mono<ExchangeRateResponse> result =  exchangeRateService.getExchangeRates(base);
		
		StepVerifier.create(result)
					.expectNextMatches(t -> {
						 assertEquals(2, t.getRates().size());
						 assertEquals(Codes.SUCCESS.getCode(), t.getCode());
						 assertEquals(base,t.getBase());
						 return true;
					})
					.verifyComplete();
	}
	
	@Test
	public void getExchangeRates_Fail_WebClientRequestException() {
		String base = "EUR";
		ExchangeRateCCResponse mockExchangeRateCCResponse = getMockExchangeRateCCResponse(base,true);
		setMockGetExchangeRatesForBaseFromCache(base,null);
       
		WebClientRequestException mockWebClientRequestException = mock(WebClientRequestException.class);
        CurrencyConverterInternalException mockCurrencyConverterInternalException = mock(CurrencyConverterInternalException.class);
		
		when(exchangeRateConnector.getExchangeRates(base)).thenReturn(Mono.error(mockWebClientRequestException));
		when(currencyConverterHandleException.mapWebClientException(mockWebClientRequestException)).thenReturn(mockCurrencyConverterInternalException);
        when(currencyConverterMapper.getExchangeRateResponse(mockExchangeRateCCResponse)).thenReturn(realCurrencyConverterMapper.getExchangeRateResponse(mockExchangeRateCCResponse));
       
        setMockGetExchangeRateNoDataResponse();
		
        StepVerifier.create(exchangeRateService.getExchangeRates(base))
					.expectError(CurrencyConverterInternalException.class)
					.verify();
    }
	
	@Test
	public void getExchangeRates_Fail_WebClientResponseException() {
		String base = "EUR";
		ExchangeRateCCResponse mockExchangeRateCCResponse = getMockExchangeRateCCResponse(base,true);
		setMockGetExchangeRatesForBaseFromCache(base,null);
       
		WebClientResponseException mockWebClientResponseException = mock(WebClientResponseException.class);
        CurrencyConverterInternalException mockCurrencyConverterInternalException = mock(CurrencyConverterInternalException.class);
		
		when(exchangeRateConnector.getExchangeRates(base)).thenReturn(Mono.error(mockWebClientResponseException));
		when(currencyConverterHandleException.mapWebClientException(mockWebClientResponseException)).thenReturn(mockCurrencyConverterInternalException);
        when(currencyConverterMapper.getExchangeRateResponse(mockExchangeRateCCResponse)).thenReturn(realCurrencyConverterMapper.getExchangeRateResponse(mockExchangeRateCCResponse));
       
        setMockGetExchangeRateNoDataResponse();
		
        StepVerifier.create(exchangeRateService.getExchangeRates(base))
					.expectError(CurrencyConverterInternalException.class)
					.verify();
    }
	
	
	@Test
	public void getExchangeRates_Fail_CurrencyConverterInternalException() {
		String base = "EUR";
		when(exchangeRateConnector.getExchangeRates(base)).thenReturn(null);
		assertThrows(CurrencyConverterInternalException.class, () -> {
            exchangeRateService.getExchangeRates(base).block(); 
        });
	}
	
	
	
	@Test
	public void getSymbols_Success() {
		setMockedSupportedSymbolsCCResponse(true,false);
		setMockedGetSymbolsNoDataResponse();
		
		Mono<SupportedSymbolsResponse> result =  exchangeRateService.getSymbols();
		
		StepVerifier.create(result)
					.expectNextMatches(t -> t.getSymbols().size() == getSymbols().size())
					.verifyComplete();
	}
	
	@Test
	public void getSymbols_No_Data_Error() {
		setMockedSupportedSymbolsCCResponse(false,false);
		setMockedGetSymbolsNoDataResponse();
		
		Mono<SupportedSymbolsResponse> result =  exchangeRateService.getSymbols();
		
		StepVerifier.create(result)
					.expectNextMatches(t -> t.getCode().equals(Codes.NO_DATA_ERROR.getCode()))
					.verifyComplete();
	}
	
	@Test
	public void getSymbols_From_Cache_Success() {
		setMockedSupportedSymbolsCCResponse(true,true);
		
		Mono<SupportedSymbolsResponse> result =  exchangeRateService.getSymbols();
		
		StepVerifier.create(result)
					.expectNextMatches(t -> t.getSymbols().size() == getSymbols().size())
					.verifyComplete();
	}
	
	@Test
	public void getSymbols_Fail_CurrencyConverterInternalException() {
		when(exchangeRateConnector.getSymbols()).thenReturn(null);
		assertThrows(CurrencyConverterInternalException.class, () -> {
            exchangeRateService.getSymbols().block(); 
        });
	}
	
	@Test
	public void getSymbols_Fail_WebClientRequestException() {
		SupportedSymbolsCCResponse mockSupportedSymbolsCCResponse = getMockSupportedSymbolsCCResponse(true);
       
        WebClientRequestException mockWebClientRequestException = mock(WebClientRequestException.class);
        CurrencyConverterInternalException mockCurrencyConverterInternalException = mock(CurrencyConverterInternalException.class);
		
		when(exchangeRateConnector.getSymbols()).thenReturn(Mono.error(mockWebClientRequestException));
		when(currencyConverterHandleException.mapWebClientException(mockWebClientRequestException)).thenReturn(mockCurrencyConverterInternalException);
        when(currencyConverterMapper.getSymbolsResponse(mockSupportedSymbolsCCResponse)).thenReturn(realCurrencyConverterMapper.getSymbolsResponse(mockSupportedSymbolsCCResponse));
	
        setMockedGetSymbolsNoDataResponse();
		
        StepVerifier.create(exchangeRateService.getSymbols())
					.expectError(CurrencyConverterInternalException.class)
					.verify();
    }
	
	@Test
	public void getSymbols_Fail_WebClientResponseException() {
		SupportedSymbolsCCResponse mockSupportedSymbolsCCResponse = getMockSupportedSymbolsCCResponse(true);
       
        WebClientResponseException mockWebClientResponseException = mock(WebClientResponseException.class);
        CurrencyConverterInternalException mockCurrencyConverterInternalException = mock(CurrencyConverterInternalException.class);
		
		when(exchangeRateConnector.getSymbols()).thenReturn(Mono.error(mockWebClientResponseException));
		when(currencyConverterHandleException.mapWebClientException(mockWebClientResponseException)).thenReturn(mockCurrencyConverterInternalException);
        when(currencyConverterMapper.getSymbolsResponse(mockSupportedSymbolsCCResponse)).thenReturn(realCurrencyConverterMapper.getSymbolsResponse(mockSupportedSymbolsCCResponse));
		
        setMockedGetSymbolsNoDataResponse();
		
        StepVerifier.create(exchangeRateService.getSymbols())
					.expectError(CurrencyConverterInternalException.class)
					.verify();
	}
	
	private Map<String, String> getSymbols(){
		Map<String, String> symbols = new HashMap<>();
        symbols.put("AED", "AED");
        symbols.put("AFN", "AEN");
		return symbols;
	}
	
	private SupportedSymbolsCCResponse getMockSupportedSymbolsCCResponse(boolean status) {
		Map<String, String> symbols = getSymbols();
        SupportedSymbolsCCResponse mockSupportedSymbolsCCResponse = new SupportedSymbolsCCResponse();
        mockSupportedSymbolsCCResponse.setSuccess(status);
        mockSupportedSymbolsCCResponse.setSymbols(symbols);
        return mockSupportedSymbolsCCResponse;
	}
	
	private void setMockedSupportedSymbolsCCResponse(boolean status, boolean cache) {
		if(cache) {
			setMockGetSymbolsFromCache(getSymbols());
		}else {
			setMockGetSymbolsFromCache(new HashMap<String, String>());
		}
		SupportedSymbolsCCResponse mockSupportedSymbolsCCResponse = getMockSupportedSymbolsCCResponse(status);
     	when(exchangeRateConnector.getSymbols()).thenReturn(Mono.just(mockSupportedSymbolsCCResponse));
		when(currencyConverterMapper.getSymbolsResponse(mockSupportedSymbolsCCResponse)).thenReturn(realCurrencyConverterMapper.getSymbolsResponse(mockSupportedSymbolsCCResponse));
		when(currencyConverterMapper.getSymbolsResponse(getSymbols())).thenReturn(realCurrencyConverterMapper.getSymbolsResponse(getSymbols()));
	}
	
	private void setMockGetSymbolsFromCache( Map<String, String> symbols) {
		when(exchangeRateCache.getSymbols()).thenReturn(symbols);
	}
	
	private void setMockedGetSymbolsNoDataResponse() {
		when(currencyConverterMapper.getSymbolsNoDataResponse()).thenReturn(realCurrencyConverterMapper.getSymbolsNoDataResponse());
	}
	
	private Map<String, Double> getRates(){
		Map<String, Double> rates = new HashMap<>();
		rates.put("AED", 1.0);
		rates.put("AFN", 2.0);
		return rates;
	}
	
	private ExchangeRateCCResponse getMockExchangeRateCCResponse(String base, boolean status) {
		Map<String, Double> rates = getRates();
		ExchangeRateCCResponse mockExchangeRateCCResponse = new ExchangeRateCCResponse();
        mockExchangeRateCCResponse.setSuccess(status);
        mockExchangeRateCCResponse.setBase(base);
        mockExchangeRateCCResponse.setTimestamp(110000000);
        mockExchangeRateCCResponse.setDate("27-01-2011");
        mockExchangeRateCCResponse.setRates(rates);
        return mockExchangeRateCCResponse;
	}
	
	private void setMockExchangeRateCCResponse(String base, boolean status, boolean cache) {
		ExchangeRateCCResponse mockExchangeRateCCResponse = getMockExchangeRateCCResponse(base,status);
		when(exchangeRateConnector.getExchangeRates(base)).thenReturn(Mono.just(mockExchangeRateCCResponse));
		when(currencyConverterMapper.getExchangeRateResponse(base,getRates())).thenReturn(realCurrencyConverterMapper.getExchangeRateResponse(mockExchangeRateCCResponse));
		when(currencyConverterMapper.getExchangeRateResponse(mockExchangeRateCCResponse)).thenReturn(realCurrencyConverterMapper.getExchangeRateResponse(mockExchangeRateCCResponse));
		if(cache) {
			setMockGetExchangeRatesForBaseFromCache(base,getRates());
		}else {
			setMockGetExchangeRatesForBaseFromCache(base,null);
		}
	}
	
	private void setMockGetExchangeRatesForBaseFromCache(String base, Map<String, Double> rates) {
		when(exchangeRateCache.getExchangeRatesForBase(Mockito.anyString())).thenReturn(rates);
	}
	
	private void setMockGetExchangeRateNoDataResponse() {
		when(currencyConverterMapper.getExchangeRateNoDataResponse()).thenReturn(realCurrencyConverterMapper.getExchangeRateNoDataResponse());
	}
	
	
}