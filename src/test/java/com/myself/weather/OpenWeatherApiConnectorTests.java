package com.myself.weather;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.myself.weather.connector.OpenWeatherApiConnector;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenWeatherApiConnectorTests {
	
	@Autowired
    private OpenWeatherApiConnector openWeatherApiConnector;
	
	@Test
	public void testFetch() {
		JsonNode apiRes = openWeatherApiConnector.fetch("/forecast?q={city}", "Milan");
		Assert.assertNotNull(apiRes);
		Assert.assertNotNull(apiRes.get("list"));
	}
	
}
