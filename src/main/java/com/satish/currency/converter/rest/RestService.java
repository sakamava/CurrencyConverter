package com.satish.currency.converter.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.satish.currency.converter.model.request.ConversionRequest;
import com.satish.currency.converter.model.response.ConversionResponse;
import com.satish.currency.converter.model.response.ExchangeRateResponse;
import com.satish.currency.converter.model.response.SupportedSymbolsResponse;
import com.satish.currency.converter.service.IExchangeRateService;

import reactor.core.publisher.Mono;

/**
 * Rest Service
 */
@RestController
@RequestMapping("/api/v1")
@Validated
public class RestService {

	@Autowired
	private IExchangeRateService exchangeRateService;
	
	@GetMapping("/symbols")
	public Mono<SupportedSymbolsResponse> getSymbols() {
		return exchangeRateService.getSymbols();
	}

	@GetMapping("/exchangeRates")
	public Mono<ExchangeRateResponse> getExchangeRates(@RequestParam(name = "base") @NotBlank(message = "base name cannot be blank") String base) {
		return exchangeRateService.getExchangeRates(base);
	}

	@PostMapping("/convert")
	public Mono<ConversionResponse> convertCurrency(@Valid @RequestBody ConversionRequest request) {
		return exchangeRateService.performConversion(request);
	}
	
/**	@PostMapping("/convert1")
	public Mono<ResponseEntity<ConversionResponse>> convertCurrency1(@Valid @RequestBody ConversionRequest request) {
		return exchangeRateService.performConversion(request).map((cc) ->{
			return ResponseEntity.ok().headers(new HttpHeaders()).body(cc);
		});
	}*/
	
	/**
	@PostMapping("/convert2")
	public ResponseEntity<ConversionResponse> convertCurrency2(@Valid @RequestBody ConversionRequest request) {
		return exchangeRateService.performConversion(request).map((cc) ->{
			return ResponseEntity.ok().headers(new HttpHeaders()).body(cc);
		}).block();
	}*/
	
}
