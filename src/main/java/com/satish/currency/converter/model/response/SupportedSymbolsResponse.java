package com.satish.currency.converter.model.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportedSymbolsResponse extends Status{

    private Map<String, String> symbols;

	
    public Map<String, String> getSymbols() {
		return symbols;
	}

	public void setSymbols(Map<String, String> symbols) {
		this.symbols = symbols;
	}
}
