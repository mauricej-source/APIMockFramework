package com.wmf.model;

/**
 * Class: ResponseBody:  
 * Scope: Model Object for API Response
 * ------------------------------------------------------------
 * Date         Resource  Description
 * ------------------------------------------------------------
 * 12/02/2021   MJOJG     INIT...          
 * ------------------------------------------------------------
 */
public class ResponseBody {
	private String content;

    public ResponseBody() {
    	this.content = null; 
    }

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		//log.info("Within the ResponseBody...\n");
		StringBuilder modelObject = new StringBuilder();
		
		modelObject.append("{\n");
		modelObject.append("ResponseBody: {\n");
		modelObject.append("content: ").append(content).append("\n");	
		modelObject.append("}").append("\n");
		modelObject.append("}");
		
		return modelObject.toString();
	}
}
