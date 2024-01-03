package com.satish.currency.converter.cache;

import java.util.Map;
 
public interface IExchangeRateCache
{
	Map<String,Double> getExchangeRatesForBase(String base);
	
	void addExchangeRatesForBase(String base,Map<String,Double> rates);
	
	Double getExchangeRate(String sourceBase, String targetBase);
	
	void addSymbols( Map<String, String> symbols) ;
	
	Map<String, String> getSymbols() ;
	
	boolean isSymbolPresent(String base);

}