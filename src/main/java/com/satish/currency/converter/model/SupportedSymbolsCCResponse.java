package com.satish.currency.converter.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportedSymbolsCCResponse {

    private boolean success;

    private Map<String, String> symbols;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
    public Map<String, String> getSymbols() {
		return symbols;
	}

	public void setSymbols(Map<String, String> symbols) {
		this.symbols = symbols;
	}

	@Override
    public String toString() {
        return "SupportedSymbols{" +
                "success=" + success +
                ", symbols=" + symbols +
                '}';
    }
	
}
