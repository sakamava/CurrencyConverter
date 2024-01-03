package com.satish.currency.converter.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableCaching
public class AppConfig {

	//TODO read timeout values from configuration properties.
	
	 @Bean
	 public WebClient webClient(@Value("${exchange-rate.api.url}") String apiUrl) {
		HttpClient httpClient = HttpClient.create()
	                .responseTimeout(Duration.ofMillis(5000))
	                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10))
	                        .addHandlerLast(new WriteTimeoutHandler(10)));
	    WebClient webClient = WebClient.builder()
	      .baseUrl(apiUrl)
	      .clientConnector(new ReactorClientHttpConnector(httpClient))
	      .build();
		return webClient;
	 }

	 
}
