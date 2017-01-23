package com.unai.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.select()
				.apis(RequestHandlerSelectors.any())
				//.paths(PathSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/error")))
				.build()
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		Contact contact = new Contact("Unai Perez", "https://github.com/unaipme", "unaipme@gmail.com");
		return new ApiInfo(
				"NoSQLTester",
				"Rest API with endpoints with which test different NoSQL systems, such as Redis or Neo4j.\n\n"
				+ "Below, a list of all APIs that the app makes available. In big letters, the name of the file. "
				+ "If you click on any of the file names, a list of all API endpoints that are written in that file is listed. "
				+ "All endpoints have a brief explanation on the right side of their boxes. If you click on the box, "
				+ "it's possible to see where in the file is this method written, and you can try it to see how it works.\n\n"
				+ "The general convention for this API is that GET requests are to retrieve data from the databases, "
				+ "POST requests are to create data, PUT requests are to create and/or update (depending on the database) "
				+ "and DELETE requests are to remove data.",
				"1.0.RELEASE",
				"",
				contact,
				"MIT License",
				"https://github.com/unaipme/NoSQLTester/blob/master/LICENSE");
	}
	
}
