package com.myself.weather.service;

import com.myself.weather.dto.Forecast;

/**
 * Interface describing Weather Service features
 * @author andreadilisio
 *
 */
public interface IWeatherService {

	/**
	 * Execute a call towards OpenWeather /forecast api and returned an object holding computed information
	 * @param city required value
	 * @return an object containing average data as per application requirement 
	 */
	Forecast fetchWeatherStatisticsByCity(String city);
}
