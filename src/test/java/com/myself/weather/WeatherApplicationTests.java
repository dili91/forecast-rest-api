package com.myself.weather;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.myself.weather.dto.Forecast;
import com.myself.weather.service.impl.WeatherService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherApplicationTests {
	
	@Autowired
    private WeatherService weatherService;

	@Test
	public void testAverageTemperature() {
		Forecast at = weatherService.fetchWeatherStatisticsByCity("New York");
		Assert.assertNotNull(at);
	}

}
