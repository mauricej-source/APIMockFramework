package com.wmf.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

/**
 * Class:  WmfMappingValidator
 * Scope:  To provide the MOCKAPI Request Response Object Mapping
 *         Validation
 * ------------------------------------------------------------
 * Date         Resource            Description
 * ------------------------------------------------------------
 * 02/15/2022   Maurice Johnson     INIT...         
 * ------------------------------------------------------------
 */
public class WmfMappingValidator {
	private String _message;
	private StringBuilder _processingMessages;
	private HttpStatus responseHTTPStatus;
	
	private static final Logger log = LogManager.getLogger(WmfMappingValidator.class);
	
    public WmfMappingValidator() {
    	this._message = new String();
    	this._processingMessages = new StringBuilder();
    	this.responseHTTPStatus = HttpStatus.OK;
    }
	
	public HttpStatus getResponseHTTPStatus(){
		return this.responseHTTPStatus;
	}
	
	public String getValidationMessages() {
		return this._processingMessages.toString();
	}
	
	public boolean validateRequestBody(String apiHTTPMethod, String apiHTTPReqBody) {
		Boolean retVal = true;
		
		// TODO Auto-generated method stub
		
		return retVal;
	}
}
