package com.myself.weather.rest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myself.weather.bean.Forecast;
import com.myself.weather.bean.RestErrorWrapper;
import com.myself.weather.service.IWeatherService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Class that provide a REST endpoint for the application registered at /weather
 * path This class depends upon a weather service to get data from OpenWeather
 * API and compute average measurements on temperature and pressure data.
 * 
 * @author andreadilisio
 *
 */
@RestController
@Validated
@RequestMapping("/weather/")
public class WeatherController {

	/* Holds weather service instance */
	@Autowired
	private IWeatherService weatherService;

	@RequestMapping(method = RequestMethod.GET, path = "/data", produces = "application/json")
	@ApiOperation("Fetch the average pressure, daily and nightly temperature in Celsius for the given city for the following 3 days")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = Forecast.class),
			@ApiResponse(code = 400, message = "Validation error", response = RestErrorWrapper.class),
			@ApiResponse(code = 404, message = "City not found", response = RestErrorWrapper.class),
			@ApiResponse(code = 500, message = "Generic error", response = RestErrorWrapper.class) })
	public Forecast fetchAverageTemperatureByCity(
			@RequestParam(value = "city", required = true) @NotEmpty @Pattern(regexp = "^[a-zA-Z, ]*$") String city) {
		return weatherService.fetchWeatherForecastByCity(city);
	}
}
