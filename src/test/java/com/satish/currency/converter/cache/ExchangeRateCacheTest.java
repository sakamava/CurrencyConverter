package com.satish.currency.converter.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
public class ExchangeRateCacheTest 
{
	@Autowired
	IExchangeRateCache exchangeRateCache;
	
	@Test
	@Order(1)
	public void getExchangeRates_SourceBase_Not_Found() {
		String sourceBase = "EUR";
		String targetBase =  "IND";
		
		Map<String, Double> ratesFromCache = exchangeRateCache.getExchangeRatesForBase(sourceBase);
		System.out.println("ratesFromCache"+ratesFromCache);
		assertNull(ratesFromCache);
		
		Double value = exchangeRateCache.getExchangeRate(sourceBase,targetBase);
		assertNull(value);
	}
	
	@Test
	@Order(2)
	public void getExchangeRates_TargetBase_Not_Found() {
		String sourceBase = "EUR";
		String targetBase =  "IND";
		Map<String, Double> rates = getRates();
		
		exchangeRateCache.addExchangeRatesForBase(sourceBase, rates);
			
		Double value = exchangeRateCache.getExchangeRate(sourceBase,targetBase);
		assertNull(value);
	}
	
	@Test
	@Order(3)
	public void getExchangeRates_Success() {
		String sourceBase = "EUR";
		String targetBase =  "AED";
		Map<String, Double> rates = getRates();
		
		exchangeRateCache.addExchangeRatesForBase(sourceBase, rates);
		Map<String, Double> ratesFromCache = exchangeRateCache.getExchangeRatesForBase(sourceBase);
	
		assertEquals(rates, ratesFromCache);
		
		Double value = exchangeRateCache.getExchangeRate(sourceBase,targetBase);
		assertEquals(getRates().get(targetBase), value);
	}
	
	@Test
	@Order(4)
	public void getSymbols_Not_Found() {
		String sourceBase = "EUR";
		
		Map<String, String> symbolsFromCache = exchangeRateCache.getSymbols();
		assertTrue(symbolsFromCache.size()==0);
		
		boolean value = exchangeRateCache.isSymbolPresent(sourceBase);
		assertFalse(value);
	}
	
	@Test
	@Order(5)
	public void getSymbols_Success() {
		String sourceBase = "EUR";
		Map<String,String> symbols = getSymbols();
		
		exchangeRateCache.addSymbols(symbols);
		
		Map<String, String> symbolsFromCache = exchangeRateCache.getSymbols();
		assertEquals(symbols,symbolsFromCache);
		
		boolean value = exchangeRateCache.isSymbolPresent(sourceBase);
		assertTrue(value);
	}
	
	private Map<String, Double> getRates(){
		Map<String, Double> rates = new HashMap<>();
		rates.put("AED", 1.0);
		rates.put("AFN", 2.0);
		return rates;
	}

	private Map<String, String> getSymbols(){
		Map<String, String> symbols = new HashMap<>();
        symbols.put("EUR", "AED");
        symbols.put("AFN", "AEN");
		return symbols;
	}
	
	
	
}