package com.myself.weather;

import java.util.Arrays;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableCaching
@EnableScheduling
@Import(BeanValidatorPluginsConfiguration.class)
public class Config {

	public static final String CACHE_NAME = "forecast";
	private static final long CACHE_EXPIRY_MS = 15000;

	@Value("${swagger.apiTitle}")
	private String apiTitle;
	@Value("${swagger.apiDescription}")
	private String apiDescription;
	@Value("${swagger.apiVersion}")
	private String apiVersion;
	@Value("${swagger.apiDeveloperName}")
	private String apiDeveloperName;
	@Value("${swagger.apiDeveloperWebsite}")
	private String apiDeveloperWebsite;
	@Value("${swagger.apiDeveloperEmail}")
	private String apiDeveloperEmail;

	/**
	 * Swagger documentation setup
	 * 
	 * @return
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select()
				.apis(RequestHandlerSelectors.basePackage("com.myself")).paths(PathSelectors.any()).build()
				.apiInfo(new ApiInfo(apiTitle, apiDescription, apiVersion, "",
						new Contact(apiDeveloperName, apiDeveloperWebsite, apiDeveloperEmail), "", "",
						Arrays.asList()));
	}

	@CacheEvict(allEntries = true, value = CACHE_NAME)
	@Scheduled(fixedDelay = CACHE_EXPIRY_MS, initialDelay = 500)
	public void reportCacheEvict() {
		LoggerFactory.getLogger(this.getClass().getName()).info("cash flushed");
	}
}