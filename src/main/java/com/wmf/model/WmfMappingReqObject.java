package com.wmf.model;

/**
 * Class: WmfMappingReqObject:  
 * Scope: Model Object for MOCKAPI HTTP Request Mapping
 * ------------------------------------------------------------
 * Date         Resource  Description
 * ------------------------------------------------------------
 * 12/02/2021   MJOJG     INIT...          
 * ------------------------------------------------------------
 */
public class WmfMappingReqObject {
    private String url;
    private String method;
    
    public WmfMappingReqObject() {
    	this.url = null; 
    	this.method = null; 
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	@Override
	public String toString() {
		//log.info("Within the WmfMappingReqObject...\n");
		StringBuilder modelObject = new StringBuilder();
		
		modelObject.append("\"request\": {\n");
		modelObject.append("\"url\": \"").append(url).append("\",\n");
		modelObject.append("\"method\": \"").append(method).append("\"\n");
		modelObject.append("}");
		
		return modelObject.toString();
	}	
}
