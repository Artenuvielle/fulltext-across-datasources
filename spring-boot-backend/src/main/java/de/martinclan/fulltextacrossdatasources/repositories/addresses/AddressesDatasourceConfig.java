package de.martinclan.fulltextacrossdatasources.repositories.addresses;

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
        basePackages = "de.martinclan.fulltextacrossdatasources.repositories.addresses",
        entityManagerFactoryRef = "addressesEntityManagerFactory",
        transactionManagerRef = "addressesTransactionManager"
)
public class AddressesDatasourceConfig {
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.addresses")
    public DataSourceProperties addressesDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.addresses")
    public DataSource addressesDataSource(
            @Qualifier("addressesDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean addressesEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                                @Qualifier("addressesDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(dataSource)
                .packages("de.martinclan.fulltextacrossdatasources.entities.addresses")
                .persistenceUnit("addressesDataSource")
                .properties(properties)
                .build();
    }

    @Bean
    public PlatformTransactionManager addressesTransactionManager(
            @Qualifier("addressesEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
