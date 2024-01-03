package com.satish.currency.converter.exception.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.satish.currency.converter.exception.CurrencyConverterException;
import com.satish.currency.converter.model.response.Status;
import com.satish.currency.converter.util.CurrencyConverterMapper;

import reactor.core.publisher.Mono;

@ControllerAdvice
public class CurrencyConverterExceptionHandler {

	@Autowired
	CurrencyConverterMapper currencyConverterMapper;
	
    @ExceptionHandler(CurrencyConverterException.class)
    public ResponseEntity<Status> handleException(CurrencyConverterException e) {
    	// Log exception
    	e.printStackTrace();
    	return ResponseEntity.status(HttpStatus.OK)
                .body(currencyConverterMapper.getCustomServerError(e.getErrorCode(),e.getMessage()));
    }
    
	
	//  @ExceptionHandler(CurrencyConverterException.class) 
	  public Mono<ResponseEntity<Status>> handleException1(CurrencyConverterException e) {
		  // Log exception e.printStackTrace(); return
		 return  Mono.just(ResponseEntity.status(HttpStatus.OK)
		  .body(currencyConverterMapper.getCustomServerError(e.getErrorCode(),e.
		  getMessage()))); 
	  }
	 
    
}