/*
 * Copyright 2016 Emmanuel Lecomte
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *  
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package fr.elecomte.garden;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.elecomte.garden.Service.Packages;
import fr.elecomte.garden.Service.ServiceConfiguration;
import fr.elecomte.garden.services.rest.mappers.LocalDateModule;
import fr.elecomte.garden.services.rest.mappers.LocalDateTimeModule;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author elecomte
 * @since 0.1.0
 */
@EnableSwagger2
@EnableAutoConfiguration
@Import(ServiceConfiguration.class)
@ComponentScan({ Packages.ROOT })
public class Service {

	static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SpringApplication.run(Service.class, args);

	}

	/**
	 * @author elecomte
	 * @since 0.1.0
	 */
	@Configuration
	public static class ServiceConfiguration extends WebMvcConfigurerAdapter {

		/**
		 * @return
		 */
		@Bean
		@Profile("swagger")
		public Docket swaggerApi() {

			LOGGER.info("SWAGGER UI activated for REST service testing on http://localhost:8080/swagger-ui.html");
			return new Docket(DocumentationType.SWAGGER_2)
					.select()
					.apis(RequestHandlerSelectors.any())
					.paths(PathSelectors.regex("/.*"))
					.build()
					.directModelSubstitute(LocalDate.class, String.class)
					.directModelSubstitute(LocalDateTime.class, String.class);
		}

		/**
		 * @param converters
		 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureMessageConverters(java.util.List)
		 */
		@Override
		public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

			// String
			StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
			converters.add(stringConverter);

			// HTTP form
			FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
			converters.add(formConverter);

			// JSON
			MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
			converters.add(jsonConverter);
			setModules(jsonConverter.getObjectMapper());

			// XML
			MappingJackson2XmlHttpMessageConverter xmlConverter = new MappingJackson2XmlHttpMessageConverter();
			converters.add(xmlConverter);
			setModules(xmlConverter.getObjectMapper());

			LOGGER.debug("Jackson modules configured");
		}

		/**
		 * @param mapper
		 */
		private static void setModules(ObjectMapper mapper) {
			mapper.registerModule(new LocalDateModule());
			mapper.registerModule(new LocalDateTimeModule());

			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		}
	}

	interface Packages {
		String ROOT = "fr.elecomte.garden";
		String REST = ROOT + ".services.rest";
	}

}
