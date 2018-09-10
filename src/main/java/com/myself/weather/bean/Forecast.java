package com.myself.weather.bean;

import javax.validation.constraints.NotNull;

/**
 * DTO to be returned to clients.
 * 
 * @author andreadilisio
 *
 */
public class Forecast {
	
	@NotNull
	private String city;
	@NotNull
	private Metrics metrics;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Metrics getMetrics() {
		return metrics;
	}

	public void setMetrics(Metrics metrics) {
		this.metrics = metrics;
	}

	public static class Metrics {
		private Double averageDailyTemperature;
		private Double averageNightlyTemperature;
		private Double averagePressure;

		public Double getAverageDailyTemperature() {
			return averageDailyTemperature;
		}

		public void setAverageDailyTemperature(Double averageDailyTemperature) {
			this.averageDailyTemperature = averageDailyTemperature;
		}

		public Double getAverageNightlyTemperature() {
			return averageNightlyTemperature;
		}

		public void setAverageNightlyTemperature(Double averageNightlyTemperature) {
			this.averageNightlyTemperature = averageNightlyTemperature;
		}

		public Double getAveragePressure() {
			return averagePressure;
		}

		public void setAveragePressure(Double averagePressure) {
			this.averagePressure = averagePressure;
		}
	}

}
