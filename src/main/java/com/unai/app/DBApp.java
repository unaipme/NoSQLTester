package com.unai.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DBApp {
	
	@Value("${redis.server.ip}")
	private String redisIP;
	
	@Value("${redis.server.port}")
	private Integer port;
	
	public static void main(String [] args) {
		SpringApplication.run(DBApp.class, args);
	}
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		Contact contact = new Contact("Unai Perez", "https://github.com/unaipme", "unaipme@gmail.com");
		return new ApiInfo(
				"NoSQLTester",
				"",
				"",
				"",
				contact,
				"",
				"");
	}
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory f = new JedisConnectionFactory();
		f.setHostName(redisIP);
		f.setPort(port);
		return f;
	}
	
	@Primary
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> t = new RedisTemplate<String, Object>();
		t.setConnectionFactory(jedisConnectionFactory());
		//t.setKeySerializer(new StringRedisSerializer());
		//t.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		t.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return t;
	}
	
}
