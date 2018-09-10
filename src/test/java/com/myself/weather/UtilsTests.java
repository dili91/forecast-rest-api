package com.myself.weather;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests for utilities class
 * 
 * @author andreadilisio
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilsTests {
	
	@Test
	public void testEpochTimeToUTCHourConversion() {
		Assert.assertEquals(21, Utils.getHour(1000*1536613506l));
		Assert.assertEquals(10, Utils.getHour(1536573993000l));
	}
	

}
