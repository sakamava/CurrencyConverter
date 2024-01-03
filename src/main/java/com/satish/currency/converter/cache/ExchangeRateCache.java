package com.satish.currency.converter.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ExchangeRateCache implements IExchangeRateCache 
{

	private Map<String, Map<String, Double>> exchangeRates = new ConcurrentHashMap<String, Map<String, Double>>();
	
	private Map<String, String> symbols = new ConcurrentHashMap<String,String>();
	
	@Override
	public Map<String, Double> getExchangeRatesForBase(String base) {
		//Optional<Map<String, Double>> exchageRates = Optional.ofNullable(this.exchangeRates.get(base));
		return this.exchangeRates.get(base);
	}

	@Override
	public void addExchangeRatesForBase(String base, Map<String, Double> rates) {
		this.exchangeRates.put(base, rates);
	}
	
	@Override
	public Double getExchangeRate(String sourceBase, String targetBase) {
	//	Optional<Double> value = Optional.ofNullable(this.exchangeRates.get(sourceBase)).map(rates -> rates.get(targetBase));
		Map<String, Double> rates = this.exchangeRates.get(sourceBase);
		if(rates!=null) {
			return rates.get(targetBase);
		}
		return null;
	}
	
	@Override
	public void addSymbols( Map<String, String> symbols) {
		this.symbols.putAll(symbols);
	}
	
	@Override
	public Map<String, String> getSymbols() {
		return this.symbols;
	}
	
	@Override
	public boolean isSymbolPresent(String base) {
		return this.symbols.containsKey(base);
	}
}