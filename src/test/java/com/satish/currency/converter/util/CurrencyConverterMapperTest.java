package com.satish.currency.converter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.satish.currency.converter.model.response.Status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CurrencyConverterMapperTest {
	
	@Autowired
	CurrencyConverterMapper currencyConverterMapper;
	
	@Test
	public void getInternalServerError() {
		Status status = new Status();
		status.setCode(Codes.INTERNAL_SERVER_ERROR.getCode()); 
		status.setMessage(Codes.INTERNAL_SERVER_ERROR.getDescription());
		
		Status actualStatus = currencyConverterMapper.getInternalServerError();
		
		assertEquals(status.getCode(), actualStatus.getCode());
		assertEquals(status.getMessage(), actualStatus.getMessage());
	}
	
	@Test
	public void getNoDataError() {
		Status status = new Status();
		status.setCode(Codes.NO_DATA_ERROR.getCode());
		status.setMessage(Codes.NO_DATA_ERROR.getDescription());
		
		Status actualStatus = currencyConverterMapper.getNoDataError();
		
		assertEquals(status.getCode(), actualStatus.getCode());
		assertEquals(status.getMessage(), actualStatus.getMessage());
	}
	
	@Test
	public void getCustomServerError() {
		String errroCode = Codes.INTERNAL_SERVER_ERROR.getCode();
		String message =Codes.INTERNAL_SERVER_ERROR.getDescription();
		Status status = new Status();
		status.setCode(errroCode); 
		status.setMessage(message);
		
		Status actualStatus = currencyConverterMapper.getCustomServerError(errroCode,message);
		
		assertEquals(status.getCode(), actualStatus.getCode());
		assertEquals(status.getMessage(), actualStatus.getMessage());
	}
}
