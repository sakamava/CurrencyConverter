package com.satish.currency.converter.connector;

import com.satish.currency.converter.model.ExchangeRateCCResponse;
import com.satish.currency.converter.model.SupportedSymbolsCCResponse;

import reactor.core.publisher.Mono;
 
public interface IExchangeRateConnector
{
	Mono<ExchangeRateCCResponse> getExchangeRates(String base);
	
	Mono<SupportedSymbolsCCResponse> getSymbols();

}