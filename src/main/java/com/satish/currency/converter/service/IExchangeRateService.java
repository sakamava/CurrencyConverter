package com.satish.currency.converter.service;

import com.satish.currency.converter.model.request.ConversionRequest;
import com.satish.currency.converter.model.response.ConversionResponse;
import com.satish.currency.converter.model.response.ExchangeRateResponse;
import com.satish.currency.converter.model.response.SupportedSymbolsResponse;

import reactor.core.publisher.Mono;
 
public interface IExchangeRateService
{
	Mono<ExchangeRateResponse>   getExchangeRates(String base);
	
	Mono<SupportedSymbolsResponse> getSymbols();
	
    Mono<ConversionResponse> performConversion(ConversionRequest request);

}