package com.satish.currency.converter.model.response;

public class ConversionResponse extends Status{
	
    private double targetBaseValue;
	
	public double getTargetBaseValue() {
		return targetBaseValue;
	}
	public void setTargetBaseValue(double targetBaseValue) {
		this.targetBaseValue = targetBaseValue;
	}
    
}
