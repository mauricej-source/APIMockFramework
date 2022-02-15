package com.wmf.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Class:  AppConfiguration
 * Scope:  To provide the necessary Application Source 
 *         Permutations within an applications.properties file.
 * ------------------------------------------------------------
 * Date         Resource  Description
 * ------------------------------------------------------------
 * 12/02/2021   MJOJG     INIT...         
 * ------------------------------------------------------------
 */
@Configuration
@PropertySource(value = "classpath:mockapi.properties")
public class AppConfiguration {
	private static final Logger log = LogManager.getLogger(AppConfiguration.class);

	@Value("${mockapi.server.files}")
	private String mockapifiles;

	@Value("${mockapi.server.stubs}")
	private String mockapistubs;
	
	public AppConfiguration() {
		//log.info("Entering the AppConfiguration...");
	}

	public String getMockapifiles() {
		return mockapifiles;
	}

	public void setMockapifiles(String mockapifiles) {
		this.mockapifiles = mockapifiles;
	}

	public String getMockapistubs() {
		return mockapistubs;
	}

	public void setMockapistubs(String mockapistubs) {
		this.mockapistubs = mockapistubs;
	}
	
	@Override
	public String toString() {
		//log.info("Within the AppConfiguration...\n");
		StringBuilder modelObject = new StringBuilder();
		
		modelObject.append("{\n");
		modelObject.append("AppConfiguration:  {\n");
		modelObject.append("mockapifiles: ").append(mockapifiles).append("\n");
		modelObject.append("mockapistubs: ").append(mockapistubs).append("\n");
		modelObject.append("}");
		
		return modelObject.toString();
	}	
}
