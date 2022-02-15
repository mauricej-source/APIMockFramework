package com.wmf.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class: WmfMappingRespObject:  
 * Scope: Model Object for MOCKAPI HTTP Response Mapping
 * ------------------------------------------------------------
 * Date         Resource  Description
 * ------------------------------------------------------------
 * 12/02/2021   MJOJG     INIT...          
 * ------------------------------------------------------------
 */
public class WmfMappingRespObject {
    private String status;
    private Map<String, String> headers;
    private String bodyFileName;
    
    public WmfMappingRespObject() {
    	this.status = null; 
    	this.headers = new HashMap<String,String>();  
    	this.bodyFileName = null;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String,String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String,String> headers) {
		this.headers = headers;
	}

	public String getBodyFileName() {
		return bodyFileName;
	}

	public void setBodyFileName(String bodyFileName) {
		this.bodyFileName = bodyFileName;
	}
    
	@Override
	public String toString() {
		//log.info("Within the WmfMappingRespObject...\n");
		StringBuilder modelObject = new StringBuilder();
		
		modelObject.append("\"response\": {\n");
		modelObject.append("\"status\": ").append(status).append(",\n");

        modelObject.append("\"headers\": {\n"); 
        
        if(headers != null) {
        	int r = 0;
            for (Map.Entry<String,String> entry : headers.entrySet()) {
                //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                modelObject.append("\"" + entry.getKey() + "\": \"" + entry.getValue() + "\""); 
                
        		if( r < headers.size()-1 )
        			modelObject.append(", \n");
        		else
        			modelObject.append("\n");
        		
        		r = r + 1;
            }
        }
        
        modelObject.append("},\n");
		
		modelObject.append("\"bodyFileName\": \"").append(bodyFileName).append("\"\n");		
		modelObject.append("}").append("\n");
		
		return modelObject.toString();
	}	    
}
