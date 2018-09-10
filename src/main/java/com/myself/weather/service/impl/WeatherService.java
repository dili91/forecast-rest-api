package com.myself.weather.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myself.weather.connector.OpenWeatherApiConnector;
import com.myself.weather.dto.Forecast;
import com.myself.weather.dto.Forecast.Metrics;
import com.myself.weather.service.IWeatherService;
import com.myself.weather.utils.DateUtils;

/**
 * Weather service implementation. It depends on OpenWeather connector and 
 * contains a bit of business logic to compute average results. Takes advantage of
 * Java 8 stream to optimize computations.
 * 
 * @author andreadilisio
 *
 */
@Service
public class WeatherService implements IWeatherService {

	private static final String CONFIG_PREFIX = "weatherService.";

	@Autowired
	private Environment env;

	/* OpenWeather API Rest connector*/
	@Autowired
	private OpenWeatherApiConnector openWeatherApiConnector;

	/* Logger instance */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Override
	public Forecast fetchWeatherStatisticsByCity(String city) {
		JsonNode owRawResult = openWeatherApiConnector.fetch("/forecast?q={city}", city);

		ObjectMapper mapper = new ObjectMapper();
		List<JsonNode> forecastList = Arrays.asList(mapper.convertValue(owRawResult.get("list"), JsonNode[].class));

		long forecastBoundaryDtEpochMillis = getForecastBoundaryDtEpochMillis();

		List<JsonNode> forecastFilteredList = forecastList.stream()
				.filter(f -> isForecastInTarget(parseForecastDatetimeToEpochMillis(f), forecastBoundaryDtEpochMillis))
				.collect(Collectors.toList());

		double averagePressure = forecastFilteredList.stream()
				.mapToDouble(f -> f.get("main").get("pressure").asDouble()).average().orElse(0);

		double averageDailyTemperature = forecastFilteredList.stream()
				.filter(f -> isDailyForecast(parseForecastDatetimeToEpochMillis(f)))
				.mapToDouble(f -> f.get("main").get("temp").asDouble()).average().orElse(0);

		// this could be more efficient, given that if !dailyRecord then means is nightly.
		// I could use the previous stream iteration to get nightly records, but I preferred to reuse streams for readability purposes.
		double averageNightlyTemperature = forecastFilteredList.stream()
				.filter(f-> !isDailyForecast(parseForecastDatetimeToEpochMillis(f)))
				.mapToDouble(f -> f.get("main").get("temp").asDouble()).average().orElse(0);

		Forecast forecast = new Forecast();
		forecast.setCity(owRawResult.get("city").get("name").asText());
		
		Metrics metrics = new Metrics();
		metrics.setAveragePressure(averagePressure);
		metrics.setAverageDailyTemperature(averageDailyTemperature);
		metrics.setAverageNightlyTemperature(averageNightlyTemperature);
		forecast.setMetrics(metrics);
		
		return forecast;
	}
	
	/**
	 * Parse date coming from OpenWeather API to a unix epoch time value
	 * @param forecastItem json object coming from OpenWeather api
	 * @return datetime of the given forecast in Unix epoch time
	 */
	private long parseForecastDatetimeToEpochMillis(JsonNode forecastItem) {
		return 1000 * forecastItem.get("dt").asLong();
	}

	
	/**
	 * Utility to get the forecast boundary to consider in project computation. 
	 * Internally leverages on project properties configurations
	 * @return the configured forecast boundary in Unix epoch time. 
	 */
	private long getForecastBoundaryDtEpochMillis() {
		return DateUtils.nowToEpochMillis()
				+ (1000 * 60 * 60 * 24 * env.getProperty(CONFIG_PREFIX + "forecastBoundaryInDays", long.class));
	}

	/**
	 * Utility to check if a date time is of our interest or not. 
	 * The application requirement states to retrieve data of the following 3 days. 
	 * I assumed that this means to consider current date + 3 days. All checks are done in UTC timezone.
	 * 3 days interval is currently configured on a project properties file 
	 * @param forecastDtEpochMillis date time to check in Unix epoch time
	 * @param forecastBoundaryDtEpochMillis boundary to consider in Unix epoch time
	 * @return true if forecastDtEpochMillis is <= boundary date tie
	 */
	private boolean isForecastInTarget(long forecastDtEpochMillis, long forecastBoundaryDtEpochMillis) {
		boolean inTarget = forecastDtEpochMillis <= forecastBoundaryDtEpochMillis;
		if (!inTarget)
			logger.info("Dropped forecast with dt={}", forecastDtEpochMillis);
		return inTarget;
	}

	
	/**
	 * Utility to check whether the given datetime in unix timestamp
	 * belongs to a daily or a nightly day interval. This range comes
	 * from a properties file contained in the project. Considering the 
	 * application requirements I made the following assumptions: 
	 * 	- all datetime checks leverage on UTC timezone
	 * 	- condition to be part of the inverval is interval.start < datetime <= interval.end
	 * @param forecastDtEpochMillis datetine in Unix epoch time 
	 * @return a boolean indicating if the given value is considered a daily forecast 
	 */
	private boolean isDailyForecast(long forecastDtEpochMillis) {
		int hour = DateUtils.getHour(forecastDtEpochMillis);
		String[] dailyInterval = env.getProperty(CONFIG_PREFIX + "dailyInterval", "6-18").split("-");
		boolean daily = hour > Integer.valueOf(dailyInterval[0]) && hour <= Integer.valueOf(dailyInterval[1]);
		logger.info("Forecast with dt={} -> daily={}", forecastDtEpochMillis, daily);
		return daily;
	}

}
