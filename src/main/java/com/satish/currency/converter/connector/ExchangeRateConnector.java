package com.satish.currency.converter.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.satish.currency.converter.model.ExchangeRateCCResponse;
import com.satish.currency.converter.model.SupportedSymbolsCCResponse;

import reactor.core.publisher.Mono;

@Component
public class ExchangeRateConnector implements IExchangeRateConnector {
	@Autowired
	WebClient webClient;

	//TODO move below 3 variables to configuration properties class
	
	
	@Autowired
        Environment environment;
	
	@Value("${exchange-rate.api.rates-path}")
	private String latestPath;

	@Value("${exchange-rate.api.symbols-path}")
	private String symbolsPath;
	
	@Override
	public Mono<ExchangeRateCCResponse> getExchangeRates(String base) {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path(latestPath)
						.queryParam("access_key", getAccessKey())
						.build())
				.retrieve()
				.bodyToMono(ExchangeRateCCResponse.class);
	}
	
	@Override
	public Mono<SupportedSymbolsCCResponse> getSymbols() {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path(symbolsPath)
						.queryParam("access_key", getAccessKey()).build())
				.retrieve()
				.bodyToMono(SupportedSymbolsCCResponse.class);
	}
	
	private String getAccessKey() {
        return environment.getProperty("EXCHANGE_RATE_ACCESS_KEY");
    }
	
}
