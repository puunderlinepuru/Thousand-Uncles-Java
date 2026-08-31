package com.thousand_uncles.discord_bot.common.config;

import com.thousand_uncles.discord_bot.common.util.AppNotifications;
import org.springframework.beans.factory.annotation.Value;
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
        AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Data Source Config Initialized");
    }

    @Value("${spring.datasource.url}")
    private String datasourceURL;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @SuppressWarnings("unused")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(datasourceURL)
                .username(datasourceUsername)
                .password(datasourcePassword)
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
