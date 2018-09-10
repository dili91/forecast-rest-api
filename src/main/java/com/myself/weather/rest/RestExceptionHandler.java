package com.myself.weather.rest;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.myself.weather.bean.RestErrorWrapper;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String CONFIG_PREFIX = "errorCode.";

	@Autowired
	private Environment env;

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		return handleExceptionInternal(ex,
				new RestErrorWrapper(env.getProperty(CONFIG_PREFIX + "validation"), ex.getMessage()), new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<Object> handleValidationException(ConstraintViolationException ex, WebRequest request) {
		return handleExceptionInternal(ex,
				new RestErrorWrapper(env.getProperty(CONFIG_PREFIX + "validation"), ex.getMessage()), new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { Throwable.class })
	protected ResponseEntity<Object> handleGenericException(Throwable throwable, WebRequest request) {
		return handleExceptionInternal(new Exception(throwable),
				new RestErrorWrapper(env.getProperty(CONFIG_PREFIX + "generic"), throwable.getMessage()),
				new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(value = { HttpClientErrorException.class })
	protected ResponseEntity<Object> handleGenericException(HttpClientErrorException ex, WebRequest request) {
		if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			return handleExceptionInternal(ex,
					new RestErrorWrapper(env.getProperty(CONFIG_PREFIX + "cityNotFound"), "City not found"),
					new HttpHeaders(), HttpStatus.NOT_FOUND, request);
		}

		return handleExceptionInternal(ex,
				new RestErrorWrapper(env.getProperty(CONFIG_PREFIX + "openWeather"), ex.getMessage()),
				new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}