package com.satish.currency.converter.exception;

public class CurrencyConverterException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private final String errorCode;
	
        public String getErrorCode() {
		return errorCode;
	}

	public CurrencyConverterException(final String errorCode, final String message) {
        	super(message);
        	this.errorCode = errorCode;
        }
	
}
