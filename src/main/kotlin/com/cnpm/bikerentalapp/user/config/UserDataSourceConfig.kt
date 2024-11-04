package com.cnpm.bikerentalapp.user.config

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class UserDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.user")
    fun userDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun userDataSource(): DataSource {
        return userDataSourceProperties()
            .initializeDataSourceBuilder()
            .build()
    }
}