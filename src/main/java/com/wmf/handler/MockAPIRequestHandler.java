package com.wmf.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;

import com.wmf.model.WmfMappingRespObject;
import com.wmf.validation.WmfMappingValidator;

import io.micrometer.core.instrument.util.IOUtils;

/**
 * Class:  MockAPIRequestHandler
 * Scope:  To provide the MOCKAPI Request Handling Helper Methods
 * ------------------------------------------------------------
 * Date         Resource            Description
 * ------------------------------------------------------------
 * 02/15/2022   Maurice Johnson     INIT...         
 * ------------------------------------------------------------
 */
public class MockAPIRequestHandler {
	private String _message;
	private StringBuilder _processingMessages;
	private HttpStatus responseHTTPStatus;
	private WmfMappingRespObject responseObject;
	private String requestAPIHTTPMethod;
	private String requestAPIRoute;	
	private static final Logger log = LogManager.getLogger(MockAPIRequestHandler.class);
	
    public MockAPIRequestHandler() {
    	this.requestAPIHTTPMethod = null;
    	this.requestAPIRoute = null;
    	this._message = new String();
    	this._processingMessages = new StringBuilder();
    	this.responseHTTPStatus = HttpStatus.OK;
    }

	private void setResponseObject(WmfMappingRespObject responseObject) {
		this.responseObject = responseObject;
	}

	private String getRequestAPIHTTPMethod() {
		return this.requestAPIHTTPMethod;
	}

	private String getRequestAPIRoute() {
		return this.requestAPIRoute;
	}

	private void setRequestAPIHTTPMethod(String requestAPIHTTPMethod) {
		this.requestAPIHTTPMethod = requestAPIHTTPMethod;
	}

	private void setRequestAPIRoute(String requestAPIRoute) {
		this.requestAPIRoute = requestAPIRoute;
	}
	
	public HttpStatus getResponseHTTPStatus(){
		return this.responseHTTPStatus;
	}
	
	public String getValidationMessages() {
		return this._processingMessages.toString();
	}
    
	public WmfMappingRespObject getResponseObject() {
		return this.responseObject;
	}
	
	private void extractJSONObjectValues(Object inputObject, String inputObjectType) throws JSONException {
		if (inputObject instanceof JSONObject) {
			Boolean responseExtracted = false;
			this.responseObject = new WmfMappingRespObject();
			
			Iterator<?> keys = ((JSONObject) inputObject).keys();
			
			while (keys.hasNext()) {
				String key = (String) keys.next();

				if (!(((JSONObject) inputObject).get(key) instanceof JSONArray))
					if (!inputObjectType.isEmpty() && inputObjectType.trim().length() > 0) {
						if (inputObjectType.trim().equalsIgnoreCase("request")) {
							if (!key.isEmpty() && key.trim().length() > 0 && key.equalsIgnoreCase("url"))
								setRequestAPIRoute((((JSONObject) inputObject).get(key)).toString());

							if (!key.isEmpty() && key.trim().length() > 0 && key.equalsIgnoreCase("method"))
								setRequestAPIHTTPMethod((((JSONObject) inputObject).get(key)).toString());
						}

						if (inputObjectType.trim().equalsIgnoreCase("response")) {
							if (!key.isEmpty() && key.trim().length() > 0 && key.equalsIgnoreCase("status")) {
								this.responseObject.setStatus((((JSONObject) inputObject).get(key)).toString());
							}

							if (!key.isEmpty() && key.trim().length() > 0 && key.equalsIgnoreCase("bodyFileName")) {
								responseExtracted = true;
								
								this.responseObject.setBodyFileName((((JSONObject) inputObject).get(key)).toString());
							}
						}
					}
			}
			
			if(responseExtracted)
				setResponseObject(this.responseObject);
		}
	}
	
	public Boolean extractRequestMapping(String mockAPIRoute, String mockAPIHTTPMethod, String mockAPIMappingLocation) {
		Boolean retVal = false;
		Boolean foundAPIMapping = false;
		File mappingLocation = null;
		File[] mappingFiles = null;
		InputStream mappingFileIOStream = null;
		String mappingFileName = null;
		String mappingFilePath = null;
		String mappingFileContent = null;
		String url = null;
		String method = null;
		MockAPIRequestHandler reqHndlr = null;
		
		try {
			reqHndlr = new MockAPIRequestHandler();
			mappingFileName = reqHndlr.getWireMockAPIRequestMapping(mockAPIHTTPMethod, mockAPIRoute, mockAPIMappingLocation);
			
			mappingFilePath = mockAPIMappingLocation + "/" + mappingFileName;
			
			mappingFileIOStream = getClass().getClassLoader().getResourceAsStream(mappingFilePath);
			mappingFileContent = IOUtils.toString(mappingFileIOStream, StandardCharsets.UTF_8);

			log.info(MockAPIRequestHandler.class.getCanonicalName()
					+ ".extractRequestMapping(): Mapping File JSON Content: \n" + mappingFileContent);

			// Parse The JSON Object
			JSONObject json = new JSONObject(mappingFileContent);

			// Request Mapping
			JSONObject reqJSONObject = null;
			if (json.getJSONObject("request") instanceof JSONObject) {
				reqJSONObject = json.getJSONObject("request");

				extractJSONObjectValues(reqJSONObject, "request");
			}

			url = getRequestAPIRoute();
			method = getRequestAPIHTTPMethod();

			log.info(MockAPIRequestHandler.class.getCanonicalName()
					+ ".extractRequestMapping(): Mapping File URL Content: " + url);
			log.info(MockAPIRequestHandler.class.getCanonicalName()
					+ ".extractRequestMapping(): Mapping File METHOD Content: " + method);

			// Process Response Mapping
			JSONObject respJSONObject = json.getJSONObject("response");

			extractJSONObjectValues(respJSONObject, "response");
			
			retVal = true;
		} catch (JSONException e) {
			this._message = MockAPIRequestHandler.class.getCanonicalName()
					+ ".extractRequestMapping(): JSONException Thrown: " + mappingFileName;
			
			log.info(this._message);
			
			this.responseHTTPStatus = HttpStatus.BAD_REQUEST;
			this._processingMessages.append(this._message).append("\n");				
		}

		return retVal;
	}
	
	private String getWireMockAPIRequestMapping(String mockAPIHTTPMethod, String mockAPIRoute, String mockAPIFilesLocation) {
		String mappingFileName = null;
					
		if (!mockAPIRoute.isEmpty() && mockAPIRoute.trim().length() > 0) {
			if (!mockAPIHTTPMethod.isEmpty() && mockAPIHTTPMethod.trim().length() > 0) {
				if (!mockAPIFilesLocation.isEmpty() && mockAPIFilesLocation.trim().length() > 0) {
					ClassLoader loader = Thread.currentThread().getContextClassLoader();
					URL url = loader.getResource(mockAPIFilesLocation);
					String path = url.getPath();

					try {
						File[] listOfMappings = new File(path).listFiles();
						String discoveredAPIRoute = null;
						String discoveredHttpMethod = null;
						String[] urlRouteArray = null;
						String[] httpMethodArray = null;
						boolean foundURL = false;
						boolean foundHttpMethod = false;
						boolean foundFileMapping = false;
						
						for (File file : listOfMappings) {			
							foundURL = false;
							foundHttpMethod = false;
							Scanner scanner = new Scanner(file);
							
							while (scanner.hasNextLine()) {
								String line = scanner.nextLine();
								// process the line

								if (!line.isEmpty()) {
									String lineTrimmed = line.trim();

									// Examples of Line: "url": "/ab/integration/salesforce/v1/sales-reps",
								    //                   "method": "POST",
									if (lineTrimmed.contains("\"url\":")) {
										urlRouteArray = lineTrimmed.split("\"");

										if (urlRouteArray != null && urlRouteArray.length > 0) {
											discoveredAPIRoute = urlRouteArray[3];
											
											if(mockAPIRoute.equalsIgnoreCase(discoveredAPIRoute)) {
												foundURL = true;
											}
										}
									}
									
									if (lineTrimmed.contains("\"method\":")) {
										httpMethodArray = lineTrimmed.split("\"");

										if (httpMethodArray != null && httpMethodArray.length > 0) {
											discoveredHttpMethod = httpMethodArray[3];
											
											if(mockAPIHTTPMethod.equalsIgnoreCase(discoveredHttpMethod)) {
												foundHttpMethod = true;
											}
										}
									}
								}
								
								if(!foundFileMapping && foundURL && foundHttpMethod) {
									foundFileMapping = true;
									mappingFileName = file.getName();
									
									log.info(MockAPIRequestHandler.class.getCanonicalName()
											+ ".getWireMockAPIRequestMapping(): WireMock Mapping File Found: " + mappingFileName);
								}
							}
						}
						
						if(mappingFileName.isEmpty()) {
							throw new FileNotFoundException();
						}
					} catch (FileNotFoundException e) {
						this._message = MockAPIResponseHandler.class.getCanonicalName()
								+ "extractWireMockAdminContent(): FileNotFoundException Thrown...";

						log.info(this._message);

						this.responseHTTPStatus = HttpStatus.BAD_REQUEST;
						this._processingMessages.append(this._message).append("\n");
					}
				} else {
					this._message = MockAPIRequestHandler.class.getCanonicalName()
							+ ".getWireMockAPIRequestMapping(): Resource Mappings Location Does NOT MATCH or NOT FOUND: "
							+ mockAPIHTTPMethod;

					log.info(this._message);

					this.responseHTTPStatus = HttpStatus.FORBIDDEN;
					this._processingMessages.append(this._message).append("\n");
				}
			} else {
				this._message = MockAPIRequestHandler.class.getCanonicalName()
						+ ".getWireMockAPIRequestMapping(): HTTP Method Does NOT MATCH or NOT FOUND: "
						+ mockAPIHTTPMethod;

				log.info(this._message);

				this.responseHTTPStatus = HttpStatus.FORBIDDEN;
				this._processingMessages.append(this._message).append("\n");
			}
		} else {
			this._message = MockAPIRequestHandler.class.getCanonicalName()
					+ ".getWireMockAPIRequestMapping(): MOCK API Route Does NOT MATCH or NOT FOUND: " + mockAPIRoute;

			log.info(this._message);

			this.responseHTTPStatus = HttpStatus.NOT_FOUND;
			this._processingMessages.append(this._message).append("\n");
		}
		
		return mappingFileName;
	}
}
