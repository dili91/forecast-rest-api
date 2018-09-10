package com.myself.weather.connector;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class only meant to gather data from OpenWeather API. 
 * Internally uses configuration parameters to setup an OpenWeather REST client
 * 
 * @author andreadilisio
 *
 */
@Service
public class OpenWeatherApiConnector {
	
	private static final String CONFIG_PREFIX = "openWeather.";

	/* Spring REST client instance */
	private RestTemplate restTemplate;

	@Autowired
	private Environment env;
	
	/**
	 * Private constructor to be used with Spring @Autowired annotation. 
	 * Singleton management is done by Spring framework
	 */
	private OpenWeatherApiConnector() {
		
	}

	/**
	 * This is called right after the bean and its dependency are built.
	 */
	@PostConstruct
	private void init() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(env.getProperty(CONFIG_PREFIX+"connectionTimeout", Integer.class));
		requestFactory.setReadTimeout(env.getProperty(CONFIG_PREFIX+"readTimeout", Integer.class));
		restTemplate = new RestTemplate(requestFactory);
	}

	public JsonNode fetch(String path, Object... params) {
		return restTemplate.getForObject(buildAbsoluteUrl(path), JsonNode.class, params);
	}

	private String buildAbsoluteUrl(String relativePath) {
		return env.getProperty(CONFIG_PREFIX+"apiBaseUrl") + env.getProperty(CONFIG_PREFIX+"apiVersion") + relativePath
				+ "&APPID=" + env.getProperty(CONFIG_PREFIX+"apiKey") + "&units=" + env.getProperty(CONFIG_PREFIX+"units");
	}
}
