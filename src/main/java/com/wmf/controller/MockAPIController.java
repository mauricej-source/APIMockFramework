package com.wmf.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wmf.service.IMockAPIService;

/**
 * Class:  MockAPIController
 * Scope:  Provide MOCKAPI Application Inheritance
 * ------------------------------------------------------------
 * Date         Resource  Description
 * ------------------------------------------------------------
 * 12/02/2021   MJOJG     INIT...
 * ------------------------------------------------------------
 */
@RestController
@RequestMapping(path = "/**")
public class MockAPIController {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private IMockAPIService mockAPIService;
	
	private static final Logger log = LogManager.getLogger(MockAPIController.class);
	
	@GetMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mockApiGetRoute(HttpServletRequest req, HttpServletResponse res) {
		ResponseEntity<String> responseEntity = null;
		
		log.info("Entering Controller 'mockApiRoute' HTTP GET Method...");
		
		responseEntity = (ResponseEntity<String>) mockAPIService.mockApiRoute(req, res);
		
		return responseEntity;
	}
	
	@PostMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mockApiPostRoute(HttpServletRequest req, HttpServletResponse res) {
		ResponseEntity<String> responseEntity = null;
		
		log.info("Entering Controller 'mockApiRoute' HTTP POST Method...");
		
		responseEntity = (ResponseEntity<String>) mockAPIService.mockApiRoute(req, res);
		
		return responseEntity;
	}
	
	@PutMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mockApiPutRoute(HttpServletRequest req, HttpServletResponse res) {
		ResponseEntity<String> responseEntity = null;
		
		log.info("Entering Controller 'mockApiRoute' HTTP PUT Method...");
		
		responseEntity = (ResponseEntity<String>) mockAPIService.mockApiRoute(req, res);
		
		return responseEntity;
	}	
	
	@PatchMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mockApiPatchRoute(HttpServletRequest req, HttpServletResponse res) {
		ResponseEntity<String> responseEntity = null;
		
		log.info("Entering Controller 'mockApiRoute' HTTP PATCH Method...");
		
		responseEntity = (ResponseEntity<String>) mockAPIService.mockApiRoute(req, res);
		
		return responseEntity;
	}	
	
	@DeleteMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> mockApiDeleteRoute(HttpServletRequest req, HttpServletResponse res) {
		ResponseEntity<String> responseEntity = null;
		
		log.info("Entering Controller 'mockApiRoute' HTTP DELETE Method...");
		
		responseEntity = (ResponseEntity<String>) mockAPIService.mockApiRoute(req, res);
		
		return responseEntity;
	}	
}