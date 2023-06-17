package de.martinclan.fulltextacrossdatasources.repositories.patients;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = { "de.martinclan.fulltextacrossdatasources.repositories.patients" },
        entityManagerFactoryRef = "patientsEntityManagerFactory",
        transactionManagerRef = "patientsTransactionManager"
)
public class PatientsDatasourceConfig {
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.patients")
    public DataSourceProperties patientsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.patients")
    public DataSource patientsDataSource(
            @Qualifier("patientsDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean patientsEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                               @Qualifier("patientsDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(dataSource)
                .packages("de.martinclan.fulltextacrossdatasources.entities.patients")
                .persistenceUnit("patientsDataSource")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager patientsTransactionManager(
            @Qualifier("patientsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
