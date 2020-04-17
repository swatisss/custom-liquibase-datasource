package com.swati.example.liquibasedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class DataConfigiuration {

	@Autowired
	private LiquibaseProperties liquibaseProperties;

	@Bean
	@ConfigurationProperties(prefix ="spring.datasource")
	public DataSourceProperties getDataSourceProperties(){
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public DataSource getDefaultDataSource(){
		return getDataSourceProperties().initializeDataSourceBuilder()
				.type(HikariDataSource.class).build();
	}
	@LiquibaseDataSource
	@Bean
	public DataSource getLiquibaseDataSource(){
		DataSource ds = DataSourceBuilder.create().username(liquibaseProperties.getUser())
				.password(liquibaseProperties.getPassword())
				.url(liquibaseProperties.getUrl())
				.build();
		if(ds instanceof HikariDataSource){
			((HikariDataSource) ds).setMinimumIdle(2);
		}
		return ds;
	}
}
