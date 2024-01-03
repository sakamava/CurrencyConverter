package com.satish.currency.converter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.satish.currency.converter.CCApplication;

@SpringBootTest(classes = CCApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location = classpath:application-it.yml"})
@ExtendWith(SpringExtension.class)
public class IndexControllerIT {

	@LocalServerPort
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	
	HttpHeaders headers = new HttpHeaders();
	
	
	//@Test
	public void testRetrieveSymbols() throws JSONException {
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/index"),
				HttpMethod.GET, entity, String.class);

		String index = response.getBody();
		
		assertEquals("index", index);
	}
	
    private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
    
}
