package com.wmf.model;

/**
 * Class: WmfMapping:  
 * Scope: Model Object for MOCKAPI HTTP Request Mapping
 * ------------------------------------------------------------
 * Date         Resource            Description
 * ------------------------------------------------------------
 * 02/15/2022   Maurice Johnson     INIT...         
 * ------------------------------------------------------------
 */
public class WmfMapping {
	private WmfMappingReqObject requestObject;
	private WmfMappingRespObject responseObject;

    public WmfMapping() {
    	this.requestObject = null; 
    	this.responseObject = null;
    }
    
	public WmfMappingReqObject getRequestObject() {
		return requestObject;
	}

	public void setRequestObject(WmfMappingReqObject requestObject) {
		this.requestObject = requestObject;
	}

	public WmfMappingRespObject getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(WmfMappingRespObject responseObject) {
		this.responseObject = responseObject;
	}

	@Override
	public String toString() {
		//log.info("Within the WmfMapping...\n");
		StringBuilder modelObject = new StringBuilder();
		
		modelObject.append("{\n");
		modelObject.append(requestObject.toString()).append(",\n");
		modelObject.append(responseObject.toString()).append("\n");
		modelObject.append("}\n");
		
		return modelObject.toString();
	}    
}
