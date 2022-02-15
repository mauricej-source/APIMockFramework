package com.wmf.serviceimpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wmf.config.AppConfiguration;
import com.wmf.handler.MockAPIRequestHandler;
import com.wmf.handler.MockAPIResponseHandler;
import com.wmf.model.ResponseBody;
import com.wmf.model.WmfMappingRespObject;
import com.wmf.service.IMockAPIService;
import com.wmf.util.MockAPIUtil;
import com.wmf.validation.WmfMappingValidator;
/**
 * Class: MockAPIServiceImpl:  
 * Scope: To provide the Basis for MOCKAPI HTTP REQ RESP Handling
 * ------------------------------------------------------------
 * Date         Resource            Description
 * ------------------------------------------------------------
 * 02/15/2022   Maurice Johnson     INIT...         
 * ------------------------------------------------------------
 */
@Service
public class MockAPIServiceImpl implements IMockAPIService {
	@Autowired
	private RestTemplate restClient;

	@Autowired
	private AppConfiguration appconfig;
	
	@Autowired
	private Environment environmentObject;
	
	private static final Logger log = LogManager.getLogger(MockAPIServiceImpl.class);

	@Override
	public ResponseEntity<String> mockApiRoute(HttpServletRequest req, HttpServletResponse res) {
		MockAPIResponseHandler rspHndlr = null;
		MockAPIRequestHandler reqHndlr = null;
		String mockAPIResponseFileLocationName = null;
		String mockAPIResponseHTTPStatus = null;
		ResponseBody rspBdy = null;
		
		WmfMappingValidator wmfValidator = null;
		WmfMappingRespObject extractedResponseObject = null;
		
		MockAPIUtil mckUtil = new MockAPIUtil();
		
		//TODO:  Need to Compensate for API Routes with Parameterizations.
		String mockAPIRoute = mckUtil.extractPath(req);
		String apiRouteScope = mckUtil.extractPathScope(req);
		String mockAPIHTTPMethod = mckUtil.extractHTTPMethod(req);
		String apiHTTPReqBody = mckUtil.extractHTTPRequestBody(req).toString();
		
		String mockAPIFilesLocation = this.appconfig.getMockapifiles();
		String mockAPIMappingLocation = this.appconfig.getMockapistubs();
		
		log.info(MockAPIServiceImpl.class.getCanonicalName() + "mockApiRoute: " + mockAPIRoute);
		log.info(MockAPIServiceImpl.class.getCanonicalName() + "apiRouteScope: " + apiRouteScope);
		log.info(MockAPIServiceImpl.class.getCanonicalName() + "apiHTTPMethod: " + mockAPIHTTPMethod);
		log.info(MockAPIServiceImpl.class.getCanonicalName() + "apiHTTPReqBody: " + apiHTTPReqBody);
		log.info(MockAPIServiceImpl.class.getCanonicalName() + "mockAPIFilesLocation: " + mockAPIFilesLocation);
		log.info(MockAPIServiceImpl.class.getCanonicalName() + "mockAPIMappingLocation: " + mockAPIMappingLocation);

		//rspBdy = new ResponseBody();
		rspHndlr = new MockAPIResponseHandler();
		
		//---------------------------------------------------------------------------------------------
		//Check for WIREMOCK Administrative Routes
		//---------------------------------------------------------------------------------------------
		if(mockAPIRoute.equalsIgnoreCase("/wmf/__admin/mappings") || mockAPIRoute.equalsIgnoreCase("/wmf/__admin/routes")) {
			// Extract the Configured Response Content
			rspHndlr.extractWireMockAdminContent(mockAPIRoute, mockAPIMappingLocation);
			
			rspBdy = new ResponseBody();
			rspBdy.setContent(rspHndlr.getValidationMessages());
			
			return new ResponseEntity<String>(rspBdy.toString(), null, rspHndlr.getResponseHTTPStatus());
		} else {
			reqHndlr = new MockAPIRequestHandler();
			wmfValidator = new WmfMappingValidator();
			
			if (reqHndlr.extractRequestMapping(mockAPIRoute, mockAPIHTTPMethod, mockAPIMappingLocation)) {

				// TODO: HTTP Request Body Validation Possibly? Not certain at this stage.  Presently Not Occurring.
				if (wmfValidator.validateRequestBody(mockAPIHTTPMethod, apiHTTPReqBody)) {
					extractedResponseObject = reqHndlr.getResponseObject();
					mockAPIResponseHTTPStatus = extractedResponseObject.getStatus();
					mockAPIResponseFileLocationName = extractedResponseObject.getBodyFileName();

					// Extract the Configured Response Content
					rspHndlr.extractResponseMapping(mockAPIRoute, mockAPIFilesLocation, mockAPIResponseFileLocationName);

					if (rspHndlr.getResponseHTTPStatus().toString().equalsIgnoreCase(HttpStatus.OK.toString())) {
						return new ResponseEntity<String>(rspHndlr.getResponseJSONObject().toString(), null,
								rspHndlr.getResponseHTTPStatus(mockAPIResponseHTTPStatus));
					} else {
						rspBdy = new ResponseBody();
						rspBdy.setContent(rspHndlr.getValidationMessages());

						return new ResponseEntity<String>(rspBdy.toString(), null, rspHndlr.getResponseHTTPStatus());
					}
				} else {
					rspBdy = new ResponseBody();
					rspBdy.setContent(wmfValidator.getValidationMessages());

					return new ResponseEntity<String>(rspBdy.toString(), null, wmfValidator.getResponseHTTPStatus());
				}
			} else {
				rspBdy = new ResponseBody();
				rspBdy.setContent(wmfValidator.getValidationMessages());

				return new ResponseEntity<String>(rspBdy.toString(), null, reqHndlr.getResponseHTTPStatus());
			}
		}
	}
}
