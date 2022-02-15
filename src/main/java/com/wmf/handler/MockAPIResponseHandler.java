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

import com.wmf.util.MockAPIUtil;

import io.micrometer.core.instrument.util.IOUtils;

/**
 * Class:  MockAPIResponseHandler
 * Scope:  To provide the MOCKAPI Response Handling Helper Methods
 * ------------------------------------------------------------
 * Date         Resource            Description
 * ------------------------------------------------------------
 * 02/15/2022   Maurice Johnson     INIT...         
 * ------------------------------------------------------------
 */
public class MockAPIResponseHandler {
	private JSONObject responseJSONObject;
	private String responseContent;
	private String _message;
	private StringBuilder _processingMessages;
	private HttpStatus responseHTTPStatus;	
	private static final Logger log = LogManager.getLogger(MockAPIResponseHandler.class);
	
    public MockAPIResponseHandler() {
    	this.responseJSONObject = new JSONObject();
    	this.responseContent = null;
    	this._message = new String();
    	this._processingMessages = new StringBuilder();
    	this.responseHTTPStatus = HttpStatus.OK;
    }

	public String getValidationMessages() {
		return this._processingMessages.toString();
	}
	
	private void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
	
	public String getResponseContent() {
		return this.responseContent;
	}
	
	public JSONObject getResponseJSONObject() {
		return responseJSONObject;
	}

	private void setResponseJSONObject(JSONObject responseJSONObject) {
		this.responseJSONObject = responseJSONObject;
	}
	
	public HttpStatus getResponseHTTPStatus(){
		return this.responseHTTPStatus;
	}
	
	public HttpStatus getResponseHTTPStatus(String mockAPIResponseHTTPStatus) {
		MockAPIUtil util = new MockAPIUtil();
		int httpStatus = 400;
		HttpStatus responseHTTPStatus = HttpStatus.BAD_REQUEST;
		
		if (!mockAPIResponseHTTPStatus.isEmpty()) {
			if(util.isNumeric(mockAPIResponseHTTPStatus)) {
				httpStatus=Integer.parseInt(mockAPIResponseHTTPStatus);  
				
				switch (httpStatus) {
				case 100:
					responseHTTPStatus = HttpStatus.CONTINUE;
					break;
				case 101:
					responseHTTPStatus = HttpStatus.SWITCHING_PROTOCOLS;
					break;
				case 102:
					responseHTTPStatus = HttpStatus.PROCESSING;
					break;
				case 200:
					responseHTTPStatus = HttpStatus.OK;
					break;
				case 201:
					responseHTTPStatus = HttpStatus.CREATED;
					break;
				case 202:
					responseHTTPStatus = HttpStatus.ACCEPTED;
					break;
				case 203:
					responseHTTPStatus = HttpStatus.NON_AUTHORITATIVE_INFORMATION;
					break;
				case 204:
					responseHTTPStatus = HttpStatus.NO_CONTENT;
					break;
				case 205:
					responseHTTPStatus = HttpStatus.RESET_CONTENT;
					break;
				case 206:
					responseHTTPStatus = HttpStatus.PARTIAL_CONTENT;
					break;
				case 207:
					responseHTTPStatus = HttpStatus.MULTI_STATUS;
					break;
				case 208:
					responseHTTPStatus = HttpStatus.ALREADY_REPORTED;
					break;
				case 226:
					responseHTTPStatus = HttpStatus.IM_USED;
					break;	
				case 300:
					responseHTTPStatus = HttpStatus.MULTIPLE_CHOICES;
					break;	
				case 301:
					responseHTTPStatus = HttpStatus.MOVED_PERMANENTLY;
					break;	
				case 302:
					responseHTTPStatus = HttpStatus.FOUND;
					break;	
				case 303:
					responseHTTPStatus = HttpStatus.SEE_OTHER;
					break;	
				case 304:
					responseHTTPStatus = HttpStatus.NOT_MODIFIED;
					break;	
				case 305:
					responseHTTPStatus = HttpStatus.USE_PROXY;
					break;	
				case 307:
					responseHTTPStatus = HttpStatus.TEMPORARY_REDIRECT;
					break;	
				case 308:
					responseHTTPStatus = HttpStatus.PERMANENT_REDIRECT;
					break;						
				case 400:
					responseHTTPStatus = HttpStatus.BAD_REQUEST;
					break;
				case 401:
					responseHTTPStatus = HttpStatus.UNAUTHORIZED;
					break;						
				case 402:
					responseHTTPStatus = HttpStatus.PAYMENT_REQUIRED;
					break;						
				case 403:
					responseHTTPStatus = HttpStatus.FORBIDDEN;
					break;						
				case 404:
					responseHTTPStatus = HttpStatus.NOT_FOUND;
					break;						
				case 405:
					responseHTTPStatus = HttpStatus.METHOD_NOT_ALLOWED;
					break;						
				case 406:
					responseHTTPStatus = HttpStatus.NOT_ACCEPTABLE;
					break;						
				case 407:
					responseHTTPStatus = HttpStatus.PROXY_AUTHENTICATION_REQUIRED;
					break;						
				case 408:
					responseHTTPStatus = HttpStatus.REQUEST_TIMEOUT;
					break;						
				case 409:
					responseHTTPStatus = HttpStatus.CONFLICT;
					break;						
				case 410:
					responseHTTPStatus = HttpStatus.GONE;
					break;						
				case 411:
					responseHTTPStatus = HttpStatus.LENGTH_REQUIRED;
					break;						
				case 412:
					responseHTTPStatus = HttpStatus.PRECONDITION_FAILED;
					break;						
				case 413:
					responseHTTPStatus = HttpStatus.REQUEST_ENTITY_TOO_LARGE;
					break;						
				case 414:
					responseHTTPStatus = HttpStatus.REQUEST_URI_TOO_LONG;
					break;						
				case 415:
					responseHTTPStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
					break;						
				case 416:
					responseHTTPStatus = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE;
					break;						
				case 417:
					responseHTTPStatus = HttpStatus.EXPECTATION_FAILED;
					break;						
				case 418:
					responseHTTPStatus = HttpStatus.I_AM_A_TEAPOT;
					break;												
				case 422:
					responseHTTPStatus = HttpStatus.UNPROCESSABLE_ENTITY;
					break;																							
				case 426:
					responseHTTPStatus = HttpStatus.UPGRADE_REQUIRED;
					break;						
				case 428:
					responseHTTPStatus = HttpStatus.PRECONDITION_REQUIRED;
					break;						
				case 429:
					responseHTTPStatus = HttpStatus.TOO_MANY_REQUESTS;
					break;						
				case 431:
					responseHTTPStatus = HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE;
					break;																						
				case 451:
					responseHTTPStatus = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS;
					break;	
				case 500:
					responseHTTPStatus = HttpStatus.INTERNAL_SERVER_ERROR;
					break;
				case 501:
					responseHTTPStatus = HttpStatus.NOT_IMPLEMENTED;
					break;
				case 502:
					responseHTTPStatus = HttpStatus.BAD_GATEWAY;
					break;
				case 503:
					responseHTTPStatus = HttpStatus.SERVICE_UNAVAILABLE;
					break;
				case 504:
					responseHTTPStatus = HttpStatus.GATEWAY_TIMEOUT;
					break;
				case 505:
					responseHTTPStatus = HttpStatus.HTTP_VERSION_NOT_SUPPORTED;
					break;
				case 506:
					responseHTTPStatus = HttpStatus.VARIANT_ALSO_NEGOTIATES;
					break;
				case 507:
					responseHTTPStatus = HttpStatus.INSUFFICIENT_STORAGE;
					break;
				case 508:
					responseHTTPStatus = HttpStatus.LOOP_DETECTED;
					break;
				case 509:
					responseHTTPStatus = HttpStatus.BANDWIDTH_LIMIT_EXCEEDED;
					break;
				case 510:
					responseHTTPStatus = HttpStatus.NOT_EXTENDED;
					break;
				case 511:
					responseHTTPStatus = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED;
					break;					
				default:
					responseHTTPStatus = HttpStatus.BAD_REQUEST;
					break;
				}
				
			}
		}
		
		return responseHTTPStatus;
	}
	
	private void parseJSONObjectValues(Object inputObject) throws JSONException {
		if (inputObject instanceof JSONObject) {
			Boolean responseExtracted = false;

			Iterator<?> keys = ((JSONObject) inputObject).keys();

			while (keys.hasNext()) {
				String key = (String) keys.next();

				if (!(((JSONObject) inputObject).get(key) instanceof JSONArray))
					if (!key.isEmpty() && key.trim().length() > 0 && key.equalsIgnoreCase("content"))
						setResponseContent((((JSONObject) inputObject).get(key)).toString());
			}
		}
	}

	public void extractWireMockAdminContent(String apiRoute, String mockAPIFilesLocation) {
		//---------------------------------------------------------------------------------------------
		//Check for WIREMOCK Administrative Routes
		//---------------------------------------------------------------------------------------------
		if(apiRoute.equalsIgnoreCase("/wmf/__admin/mappings") || apiRoute.equalsIgnoreCase("/wmf/__admin/routes")) {
			if(apiRoute.equalsIgnoreCase("/wmf/__admin/mappings")){
			    ClassLoader loader = Thread.currentThread().getContextClassLoader();
			    URL url = loader.getResource(mockAPIFilesLocation);
			    String path = url.getPath();
			    
			    this._processingMessages.append("\n      ADMIN: Mappings List - \n");
			    
			    File[] listOfMappings = new File(path).listFiles();
				int i = 1;
				for (File file : listOfMappings) {
					int begining = file.getAbsolutePath().indexOf(".");
					int end = file.getAbsolutePath().length();
					
					String fileExtension = file.getAbsolutePath().substring(begining, end);
                    String fileName = file.getName();
                    
					this._processingMessages.append("      ").append(i).append(". ").append(fileName).append("\n");
					i = i + 1;
				}
			    	
				this._processingMessages.append("\n");
				
				this.responseHTTPStatus = HttpStatus.OK;
				
				log.info(this._processingMessages.toString());
			}
			
			if (apiRoute.equalsIgnoreCase("/wmf/__admin/routes")) {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				URL url = loader.getResource(mockAPIFilesLocation);
				String path = url.getPath();

				this._processingMessages.append("\n      ADMIN: API Route List - \n");

				try {
					File[] listOfMappings = new File(path).listFiles();
					int j = 1;
					for (File file : listOfMappings) {
						String urlRoute = null;
						String[] urlRouteArray = null;

						Scanner scanner = new Scanner(file);

						while (scanner.hasNextLine()) {
							String line = scanner.nextLine();
							// process the line

							if (!line.isEmpty()) {
								String lineTrimmed = line.trim();

								// Example of Line: "url": "/ab/integration/salesforce/v1/sales-reps",
								if (lineTrimmed.contains("\"url\":")) {
									urlRouteArray = lineTrimmed.split("\"");

									if (urlRouteArray != null && urlRouteArray.length > 0) {
										urlRoute = urlRouteArray[3];
									}
								}
							}
						}

						this._processingMessages.append("      ").append(j).append(". ").append(urlRoute).append("\n");
						j = j + 1;
					}

					this._processingMessages.append("\n");

					this.responseHTTPStatus = HttpStatus.OK;

					log.info(this._processingMessages.toString());
				} catch (FileNotFoundException e) {
					this._message = MockAPIResponseHandler.class.getCanonicalName()
							+ "extractWireMockAdminContent(): FileNotFoundException Thrown...";

					log.info(this._message);

					this.responseHTTPStatus = HttpStatus.BAD_REQUEST;
					this._processingMessages.append(this._message).append("\n");
				}
			}
		}
	}
			
	public void extractResponseMapping(String apiRoute, String mockAPIFilesLocation, String mockAPIResponseFileLocationName) {
    	Boolean foundAPIResponseFiles = false;
    	File responseFilesLocation = null;
    	File[] responseFilesFiles = null;
		InputStream responseFilesFileIOStream = null;
		String responseFilesFileName = null;
		String responseFilesFileContent = null;
		String content = null;
		
		if (!apiRoute.isEmpty() && !mockAPIFilesLocation.isEmpty() && !mockAPIResponseFileLocationName.isEmpty()) {
			try {
				responseFilesFileName = mockAPIFilesLocation + "/" + mockAPIResponseFileLocationName;
				responseFilesFileIOStream = getClass().getClassLoader().getResourceAsStream(responseFilesFileName);
				responseFilesFileContent = IOUtils.toString(responseFilesFileIOStream, StandardCharsets.UTF_8);

				log.info(MockAPIResponseHandler.class.getCanonicalName()
						+ ".extractResponseMapping(): Mapping File JSON Content: \n" + responseFilesFileContent);

				// Parse The JSON Object
				JSONObject respJSONObject = new JSONObject(responseFilesFileContent);
				setResponseJSONObject(respJSONObject);

				// Response Handling
				parseJSONObjectValues(respJSONObject);
			} catch (JSONException e) {
				this._message = MockAPIResponseHandler.class.getCanonicalName()
						+ "extractResponseMapping(): JSONException Thrown: " + responseFilesFileName;

				log.info(this._message);

				this.responseHTTPStatus = HttpStatus.BAD_REQUEST;
				this._processingMessages.append(this._message).append("\n");
			}
		} else {
			this._message = MockAPIResponseHandler.class.getCanonicalName()
					+ ".extractResponseMapping(): The Method Arguments are either NULL or Empty "
					+ "{String apiRoute, String mockAPIFilesLocation, String mockAPIResponseFileLocationName}"
					+ "{" 
					+ apiRoute
					+ ", "					
					+ mockAPIFilesLocation
					+ ", "
					+ mockAPIResponseFileLocationName
					+ "}";
			
			log.info(this._message);
			
			this.responseHTTPStatus = HttpStatus.BAD_REQUEST;
			this._processingMessages.append(this._message).append("\n");				
    	}
    }  
}
