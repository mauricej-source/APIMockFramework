package com.wmf.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Class: RestClient:  
 * Scope: To provide the Basis for MOCKAPI RestTemplate
 * ------------------------------------------------------------
 * Date         Resource  Description
 * ------------------------------------------------------------
 * 12/02/2021   MJOJG     INIT...          
 * ------------------------------------------------------------
 */
@Configuration
public class RestClientConfig {
	private static final Logger log = LogManager.getLogger(RestClientConfig.class);
	
	private List<MediaType> getSupportedMediaTypes() {
	    List<MediaType> mediaTypes;

	    mediaTypes = new ArrayList<>();

	    mediaTypes.add(MediaType.APPLICATION_PDF);
	    mediaTypes.add(MediaType.IMAGE_JPEG);
	    mediaTypes.add(MediaType.IMAGE_PNG);
	    mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
	    mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.APPLICATION_JSON);

	    return mediaTypes;
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		log.info("Entering the RestTemplate Configuration...");
		
		RestTemplate restTemplate;

		List<HttpMessageConverter<?>> messageConverters;
		StringHttpMessageConverter stringHttpMessageConverter;
		ByteArrayHttpMessageConverter arrayHttpMessageConverter;
		ResourceHttpMessageConverter resourceHttpMessageConverter;
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

		messageConverters = new ArrayList<>();
		stringHttpMessageConverter = new StringHttpMessageConverter();
		arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
		resourceHttpMessageConverter = new ResourceHttpMessageConverter();
        mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

		stringHttpMessageConverter.setWriteAcceptCharset(true);
		arrayHttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());

		messageConverters.add(stringHttpMessageConverter);
		messageConverters.add(arrayHttpMessageConverter);
		messageConverters.add(resourceHttpMessageConverter);
        messageConverters.add(mappingJackson2HttpMessageConverter);
		
		restTemplate = restTemplateBuilder.setReadTimeout(Duration.ofMillis(6000)).build();
		restTemplate.setMessageConverters(messageConverters);
		
		return restTemplate;
	}
}
