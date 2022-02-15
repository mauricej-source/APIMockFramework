package com.wmf.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Interface:  IMockAPIService
 * Scope:      Provide MOCKAPI Application Inheritance
 * ------------------------------------------------------------
 * Date         Resource  Description
 * ------------------------------------------------------------
 * 12/02/2021   MJOJG     INIT...
 * ------------------------------------------------------------
 */
@Service
public interface IMockAPIService {
	ResponseEntity<String> mockApiRoute(HttpServletRequest req, HttpServletResponse res);
}
