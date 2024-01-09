package com.satish.currency.converter.exception.handler;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.satish.currency.converter.model.response.Status;
import com.satish.currency.converter.util.Codes;
import com.satish.currency.converter.util.CurrencyConverterMapper;

@ControllerAdvice
public class CurrencyConverterConstraintViolationExceptionHandler {

    @Autowired
    CurrencyConverterMapper currencyConverterMapper;
	
    @ExceptionHandler({ConstraintViolationException.class,MethodArgumentNotValidException.class, BindException.class, WebExchangeBindException.class})
    public ResponseEntity<Status> handleException(Exception e) {
    	// Log exception
    	e.printStackTrace();
    	return ResponseEntity.status(HttpStatus.OK)
                .body(currencyConverterMapper.getCustomServerError(Codes.CONSTRAINT_VIOLATION_ERROR.getCode(),Codes.CONSTRAINT_VIOLATION_ERROR.getDescription()));
    }
    
}
