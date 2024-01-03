package com.satish.currency.converter.exception.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.satish.currency.converter.exception.CurrencyConverterInternalException;
import com.satish.currency.converter.model.response.Status;
import com.satish.currency.converter.util.CurrencyConverterMapper;

@ControllerAdvice
public class CurrencyConverterInternalExceptionHandler {

	@Autowired
	CurrencyConverterMapper currencyConverterMapper;
	
    @ExceptionHandler(CurrencyConverterInternalException.class)
    public ResponseEntity<Status> handleException(CurrencyConverterInternalException e) {
    	// Log exception
    	e.printStackTrace();
    	return ResponseEntity.status(HttpStatus.OK)
                .body(currencyConverterMapper.getInternalServerError());
    }
    
}