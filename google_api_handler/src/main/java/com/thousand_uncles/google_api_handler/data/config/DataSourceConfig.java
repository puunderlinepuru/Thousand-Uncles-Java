package com.thousand_uncles.google_api_handler.data.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@SuppressWarnings("unused")
@Profile("prod")
public class DataSourceConfig {

    DataSourceConfig(){
        System.out.println("datasourseconfig initialized");
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @SuppressWarnings("unused")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://192.168.1.2:5432/thousand_uncles_db")
                .username("admin")
                .password("mypassword")
                .build();
    }

    @Bean
    @SuppressWarnings("unused")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @SuppressWarnings("unused")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
