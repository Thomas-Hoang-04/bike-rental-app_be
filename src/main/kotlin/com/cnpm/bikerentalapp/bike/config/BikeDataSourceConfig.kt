package com.cnpm.bikerentalapp.bike.config

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class BikeDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.bike")
    fun bikeDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun bikeDataSource(): DataSource {
        return bikeDataSourceProperties()
            .initializeDataSourceBuilder()
            .build()
    }
}