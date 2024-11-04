package com.cnpm.bikerentalapp.user.config

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
    basePackages = ["com.cnpm.bikerentalapp.user"],
    entityManagerFactoryRef = "userEntityManagerFactory",
    transactionManagerRef = "userTransactionManager"
)
class UserDataSourceJPAConfig {

    @Bean
    fun userEntityManagerFactory(@Qualifier("userDataSource") dataSource: DataSource,
        builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
            return builder.dataSource(dataSource)
                .packages("com.cnpm.bikerentalapp.user")
                .persistenceUnit("user")
                .build()
    }

    @Bean
    fun userTransactionManager(
        @Qualifier("userEntityManagerFactory") entityManagerFactory: LocalContainerEntityManagerFactoryBean)
        : PlatformTransactionManager {
            return JpaTransactionManager(requireNotNull(entityManagerFactory.getObject()))
    }
}