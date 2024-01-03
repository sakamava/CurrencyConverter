package com.satish.currency.converter.util;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.satish.currency.converter.model.ExchangeRateCCResponse;
import com.satish.currency.converter.model.SupportedSymbolsCCResponse;
import com.satish.currency.converter.model.response.ConversionResponse;
import com.satish.currency.converter.model.response.ExchangeRateResponse;
import com.satish.currency.converter.model.response.Status;
import com.satish.currency.converter.model.response.SupportedSymbolsResponse;

@Component
public class CurrencyConverterMapper {

	
	public Status getInternalServerError() {
		Status status = new Status();
		status.setCode(Codes.INTERNAL_SERVER_ERROR.getCode()); //error code
		status.setMessage(Codes.INTERNAL_SERVER_ERROR.getDescription());
		return status;
	}
	
	public Status getNoDataError() {
		Status status = new Status();
		status.setCode(Codes.NO_DATA_ERROR.getCode()); //error code
		status.setMessage(Codes.NO_DATA_ERROR.getDescription());
		return status;
	}
	
	public Status getCustomServerError(String errroCode, String message) {
		Status status = new Status();
		status.setCode(errroCode); //error code
		status.setMessage(message);
		return status;
	}
	
	public ExchangeRateResponse getExchangeRateNoDataResponse() {
		ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();
		exchangeRateResponse.setCode(Codes.NO_DATA_ERROR.getCode()); //error code
		exchangeRateResponse.setMessage(Codes.NO_DATA_ERROR.getDescription());
		return exchangeRateResponse;
	}
	
	
	public ExchangeRateResponse getExchangeRateResponse(ExchangeRateCCResponse ercr) {
		ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();
		exchangeRateResponse.setCode(Codes.SUCCESS.getCode()); //success code
		exchangeRateResponse.setMessage(Codes.SUCCESS.getDescription());
		exchangeRateResponse.setBase(ercr.getBase());
		exchangeRateResponse.setRates(ercr.getRates());
		return exchangeRateResponse;
	}
	
	public ExchangeRateResponse getExchangeRateResponse(String base, Map<String,Double> rates) {
		ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();
		exchangeRateResponse.setCode(Codes.SUCCESS.getCode()); //success code
		exchangeRateResponse.setMessage(Codes.SUCCESS.getDescription());
		exchangeRateResponse.setBase(base);
		exchangeRateResponse.setRates(rates);
		return exchangeRateResponse;
	}
	
	public SupportedSymbolsResponse getSymbolsResponse(SupportedSymbolsCCResponse sscr) {
		SupportedSymbolsResponse supportedSymbolsResponse = new SupportedSymbolsResponse();
		supportedSymbolsResponse.setCode(Codes.SUCCESS.getCode()); //success code
		supportedSymbolsResponse.setMessage(Codes.SUCCESS.getDescription());
		supportedSymbolsResponse.setSymbols(sscr.getSymbols());
		return supportedSymbolsResponse;
	}
	
	public SupportedSymbolsResponse getSymbolsResponse(Map<String,String> symbols) {
		SupportedSymbolsResponse supportedSymbolsResponse = new SupportedSymbolsResponse();
		supportedSymbolsResponse.setCode(Codes.SUCCESS.getCode()); //success code
		supportedSymbolsResponse.setMessage(Codes.SUCCESS.getDescription());
		supportedSymbolsResponse.setSymbols(symbols);
		return supportedSymbolsResponse;
	}
	
	public SupportedSymbolsResponse getSymbolsNoDataResponse() {
		SupportedSymbolsResponse supportedSymbolsResponse = new SupportedSymbolsResponse();
		supportedSymbolsResponse.setCode(Codes.NO_DATA_ERROR.getCode()); //error code
		supportedSymbolsResponse.setMessage(Codes.NO_DATA_ERROR.getDescription());
		return supportedSymbolsResponse;
	}
	
	public ConversionResponse getConversionResponse(Double targetValue) {
		ConversionResponse conversionResponse = new ConversionResponse();
		conversionResponse.setCode(Codes.SUCCESS.getCode()); //success code
		conversionResponse.setMessage(Codes.SUCCESS.getDescription());
		conversionResponse.setTargetBaseValue(targetValue);
		return conversionResponse;
	}
	
	public ConversionResponse getConversionNoDataResponse() {
		ConversionResponse conversionResponse = new ConversionResponse();
		conversionResponse.setCode(Codes.NO_DATA_ERROR.getCode()); //error code
		conversionResponse.setMessage(Codes.NO_DATA_ERROR.getDescription());
		return conversionResponse;
	}
}
