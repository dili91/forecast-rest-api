package com.myself.weather;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String CONFIG_PREFIX = "errorCode.";

	@Autowired
	private Environment env;

	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		return handleExceptionInternal(ex,
				new RestErrorWrapper(env.getProperty(CONFIG_PREFIX + "validation"), ex.getMessage()), new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
		return handleExceptionInternal(ex,
				new RestErrorWrapper(env.getProperty(CONFIG_PREFIX + "generic"), ex.getMessage()), new HttpHeaders(),
				HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(value = { HttpClientErrorException.class })
	protected ResponseEntity<Object> handleGenericException(HttpClientErrorException ex, WebRequest request) {
		if(ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			return handleExceptionInternal(ex,
					new RestErrorWrapper(env.getProperty(CONFIG_PREFIX + "cityNotFound"), "City not found"),
					new HttpHeaders(), HttpStatus.NOT_FOUND, request);
		}
		
		return handleExceptionInternal(ex,
				new RestErrorWrapper(env.getProperty(CONFIG_PREFIX + "openWeather"), ex.getMessage()),
				new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}