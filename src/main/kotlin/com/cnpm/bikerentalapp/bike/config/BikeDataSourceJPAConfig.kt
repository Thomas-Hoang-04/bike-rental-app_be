package com.cnpm.bikerentalapp.bike.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.cnpm.bikerentalapp.bike", "com.cnpm.bikerentalapp.station"],
    entityManagerFactoryRef = "bikeEntityManagerFactory",
    transactionManagerRef = "bikeTransactionManager"
)
class BikeDataSourceJPAConfig {

    @Bean
    fun bikeEntityManagerFactory(@Qualifier("bikeDataSource") dataSource: DataSource,
        builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
            return builder.dataSource(dataSource)
                .packages("com.cnpm.bikerentalapp.bike", "com.cnpm.bikerentalapp.station")
                .persistenceUnit("bike+station")
                .build()
    }

    @Bean
    fun bikeTransactionManager(
        @Qualifier("bikeEntityManagerFactory") bikeEntityManagerFactory: LocalContainerEntityManagerFactoryBean)
        : PlatformTransactionManager {
            return JpaTransactionManager(requireNotNull(bikeEntityManagerFactory.getObject()))
    }

}