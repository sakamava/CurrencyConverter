package com.satish.currency.converter.model.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class ConversionRequest {
	@NotBlank(message = "sourceBase name cannot be blank")
	private String sourceBase;
	@NotBlank(message = "targetBase name cannot be blank")
	private String targetBase;
	@DecimalMin(value = "0.0", inclusive = false, message = "Value must be greater than 0.0")
    private double sourceBaseValue;
   
	
    public ConversionRequest(String sourceBase, String targetBase, double sourceBaseValue) {
    	this.sourceBase = sourceBase;
    	this.targetBase = targetBase;
    	this.sourceBaseValue = sourceBaseValue;
    }
    
    public String getSourceBase() {
		return sourceBase;
	}
	public void setSourceBase(String sourceBase) {
		this.sourceBase = sourceBase;
	}
	public String getTargetBase() {
		return targetBase;
	}
	public void setTargetBase(String targetBase) {
		this.targetBase = targetBase;
	}
	public double getSourceBaseValue() {
		return sourceBaseValue;
	}
	public void setSourceBaseValue(double sourceBaseValue) {
		this.sourceBaseValue = sourceBaseValue;
	} 
	
}
