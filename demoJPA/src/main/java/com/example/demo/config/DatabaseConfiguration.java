package com.example.demo.config;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
// application.yml 파일을 application.properties 파일로 변경
@PropertySource("classpath:/application.properties")
public class DatabaseConfiguration {	
	// hikari 빈 태그를 만들어서 설정하는 것과 같은 효과 
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	
	// hikariConfig를 참조해서 dastaSource를 얻어내는 작업
	@Bean	
	public DataSource dataSource() throws Exception {
		DataSource dataSource = new HikariDataSource(hikariConfig());
		return dataSource;
	}
	
	
}