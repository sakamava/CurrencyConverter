package com.satish.currency.converter.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.satish.currency.converter.cache.IExchangeRateCache;
import com.satish.currency.converter.connector.IExchangeRateConnector;
import com.satish.currency.converter.exception.CurrencyConverterException;
import com.satish.currency.converter.exception.CurrencyConverterInternalException;
import com.satish.currency.converter.model.request.ConversionRequest;
import com.satish.currency.converter.model.response.ConversionResponse;
import com.satish.currency.converter.model.response.ExchangeRateResponse;
import com.satish.currency.converter.model.response.SupportedSymbolsResponse;
import com.satish.currency.converter.util.Codes;
import com.satish.currency.converter.util.CurrencyConverterHandleException;
import com.satish.currency.converter.util.CurrencyConverterMapper;

import reactor.core.publisher.Mono;

@Service
public class ExchangeRateService implements IExchangeRateService {
	@Autowired
	IExchangeRateConnector exchangeRateConnector;

	@Autowired
	IExchangeRateCache exchangeRateCache;

	@Autowired
	CurrencyConverterHandleException currencyConverterHandleException;

	@Autowired
	CurrencyConverterMapper currencyConverterMapper;

	/**
	 * 1. First checks if currency rates are available in cache for a requested base
	 * 2. If available in cache, reads from cache and sends back to client
	 * 3. If not available in cache, 
	 *      a. makes a request to exchange rates API to get the currency rates for a requested base
	 *      b. Get the currency rates for a requested base
	 *      c. if response is success 
	 *      	1. add currency rates to cache and sends back to client
	 *      d. if response is success but the response payload field "success" is false
	 *          1. returns no data to client with error code E3002
	 *      e. if response is error(timeouts,not 200 http codes) 
	 *      	2. returns internal server error to client with error code E3020
	 *  4. For any unknown error happens (as all cases are handled, it should not , but safety check), internal server error to client with error code E3020
	 *  
	 *  ==== Any exceptions raised in this method are propogated to exception handler classes in this package "de.c24.finacc.klt.exception.handler" ====
	 */
	@Override
	public Mono<ExchangeRateResponse> getExchangeRates(String base) {
		try {
				Map<String, Double> exchangeRates = exchangeRateCache.getExchangeRatesForBase(base);
				if (exchangeRates == null) {
					synchronized (base) {
						exchangeRates = exchangeRateCache.getExchangeRatesForBase(base);
						if(exchangeRates == null) { 
							return exchangeRateConnector.getExchangeRates(base)
									.filter((baseObj) -> baseObj.isSuccess())
									.map(exchangeRateResponse -> {
										exchangeRateCache.addExchangeRatesForBase(base, exchangeRateResponse.getRates());
										return currencyConverterMapper.getExchangeRateResponse(exchangeRateResponse);
										}) 
									.switchIfEmpty(Mono.just(currencyConverterMapper.getExchangeRateNoDataResponse()))
								    .onErrorMap(WebClientResponseException.class, currencyConverterHandleException::mapWebClientException)
								    .onErrorMap(WebClientRequestException.class,
											currencyConverterHandleException::mapWebClientException);
						}
					}
				} 
				return Mono.just(currencyConverterMapper.getExchangeRateResponse(base, exchangeRates));
		} 
		catch (Exception excep) {
			excep.printStackTrace(); //TODO remove
			throw new CurrencyConverterInternalException("Unknown Error", excep);
		}

	}

	/**
	 * 1. Makes a request to exchange symbols API to get the symbols
	 * 2. if response is success 
	 *      	1. add currency rates to cache and sends back to client
	 * 3. if response is success but the response payload field "success" is false
	 *          1. returns no data to client with error code E3002
	 * 4. if response is error(timeouts,not 200 http codes) 
	 *      	2. returns internal server error to client with error code E3020
	 *   
	 *  ==== Any exceptions raised in this method are propogated to exception handler classes in this package "de.c24.finacc.klt.exception.handler" ====
	 */
	@Override
	public Mono<SupportedSymbolsResponse> getSymbols() {
		try {
				Map<String, String> symbolsFromCache = exchangeRateCache.getSymbols();
				if (symbolsFromCache.isEmpty()) {
					synchronized (this) {
						symbolsFromCache = exchangeRateCache.getSymbols();
						if (symbolsFromCache.isEmpty()) {
							return exchangeRateConnector.getSymbols()
									.filter((baseObj) -> baseObj.isSuccess())
									.map(symbols -> {
											exchangeRateCache.addSymbols(symbols.getSymbols());			
											return currencyConverterMapper.getSymbolsResponse(symbols);
										})
									.switchIfEmpty(Mono.just(currencyConverterMapper.getSymbolsNoDataResponse()))
									.onErrorMap(WebClientResponseException.class,
											currencyConverterHandleException::mapWebClientException)
									.onErrorMap(WebClientRequestException.class,
											currencyConverterHandleException::mapWebClientException);
						}
					}
				}
				return Mono.just(currencyConverterMapper.getSymbolsResponse(symbolsFromCache)); 
		} catch (Exception excep) {
			excep.printStackTrace(); 
			throw new CurrencyConverterInternalException("Unknown Error", excep);
		}
	}

	/**
	 * 1. Calls getExchangeRates method
	 * 2. if response is success 
	 *      	1. performs conversion logic by invoking  performConversionLogic method
	 * 3. if response is failure 
	 *          1. returns no data to client with error code E3002
	 */
	@Override
	public Mono<ConversionResponse> performConversion(ConversionRequest request) {
		return isValidBase(request.getSourceBase())
				.then(Mono.defer(() -> {
					return getExchangeRates(request.getSourceBase())
							.filter((baseObj) -> baseObj.getCode().equals(Codes.SUCCESS.getCode()))
							.map(exchangeRateResponse -> performConversionLogic(request, exchangeRateResponse))
							.switchIfEmpty(Mono.just(currencyConverterMapper.getConversionNoDataResponse()));
				}));
	}
	
	/**
	 * 
	 * @param base
	 * @return
	 */
	private Mono<Void> isValidBase(String base) {
		Mono<SupportedSymbolsResponse> supportedSymbolsResponse = getSymbols();
		return supportedSymbolsResponse
				.filter(symbols -> symbols.getSymbols().containsKey(base))
				.switchIfEmpty(Mono.error(currencyConverterHandleException::baseNotFoundException))
				.then();
	}
	
	
	/**
	 * Conversion logic = multiplication of source base value and target base value  
	 * ==== Any exceptions raised in this method are propogated to exception handler classes in this package "de.c24.finacc.klt.exception.handler" ====
	 * @param request
	 * @param exchangeRateResponse
	 * @return
	 */
	private ConversionResponse performConversionLogic(ConversionRequest request, ExchangeRateResponse exchangeRateResponse) {
		Map<String, Double> rates = exchangeRateResponse.getRates();
	    if (rates.containsKey(request.getTargetBase())) {
	        double sourceToBaseRate = rates.get(request.getTargetBase());
	        double convertedValue = sourceToBaseRate * request.getSourceBaseValue();
	        return currencyConverterMapper.getConversionResponse(convertedValue);
	    } else {
	        throw new CurrencyConverterException(Codes.BASE_CODE_NOT_FOUND.getCode(),Codes.BASE_CODE_NOT_FOUND.getDescription() + " - "+request.getTargetBase());
	    }
	}
}