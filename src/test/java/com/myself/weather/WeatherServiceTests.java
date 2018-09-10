package com.myself.weather;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myself.weather.bean.Forecast;
import com.myself.weather.service.IWeatherService;
import com.myself.weather.service.impl.WeatherService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTests {
	
	@Autowired
    private IWeatherService weatherService;
	
	private static JsonNode forecastSampleItem;
	
	@BeforeClass
	public static void config() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
	    forecastSampleItem = mapper.readTree("{\n" + 
	    		"        \"dt\":1406106000,\n" + 
	    		"        \"main\":{\n" + 
	    		"            \"temp\":298.77,\n" + 
	    		"            \"temp_min\":298.77,\n" + 
	    		"            \"temp_max\":298.774,\n" + 
	    		"            \"pressure\":1005.93,\n" + 
	    		"            \"sea_level\":1018.18,\n" + 
	    		"            \"grnd_level\":1005.93,\n" + 
	    		"            \"humidity\":87,\n" + 
	    		"            \"temp_kf\":0.26},\n" + 
	    		"        \"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\n" + 
	    		"        \"clouds\":{\"all\":88},\n" + 
	    		"        \"wind\":{\"speed\":5.71,\"deg\":229.501},\n" + 
	    		"        \"sys\":{\"pod\":\"d\"},\n" + 
	    		"        \"dt_txt\":\"2014-07-23 09:00:00\"}");
	}

	@Test
	public void testForecastDatetimeToEpochMillis() throws Exception {
		Method method = WeatherService.class.getDeclaredMethod("getForecastDatetimeInEpochMillis", JsonNode.class);
		method.setAccessible(true);
		Assert.assertEquals(1406106000l*1000, method.invoke(new WeatherService(), forecastSampleItem));
	}
	
	@Test
	public void testForecastService() {
		String city = "Milan";
		Forecast f = weatherService.fetchWeatherForecastByCity(city);
		Assert.assertNotNull(f);
		Assert.assertNotNull(f.getCity());
		Assert.assertEquals(f.getCity().toUpperCase(), city.toUpperCase());
		Assert.assertNotNull(f.getMetrics());
		Assert.assertNotNull(f.getMetrics().getAverageDailyTemperature());
		Assert.assertNotNull(f.getMetrics().getAverageNightlyTemperature());
		Assert.assertNotNull(f.getMetrics().getAveragePressure());
	}
}
