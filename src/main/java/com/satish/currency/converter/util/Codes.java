package com.satish.currency.converter.util;

public enum Codes {
//TODO read description from application properties, this will allow to change description without source code changes.
    SUCCESS("S0000", "Success"),
    BASE_CODE_NOT_FOUND("E0001", "Base Code not found"),
    NO_DATA_ERROR("E3002", "No data found"),
    CONSTRAINT_VIOLATION_ERROR("E3003", "Validation Error"),
    INTERNAL_SERVER_ERROR("E3020", "Internal Server Error");
	

    private final String code;
    private final String description;

    Codes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
    
}
