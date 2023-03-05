package com.cml.defaultnominator.config.database.namedobject;

import com.cml.defaultnominator.config.database.common.DataSourceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

import static com.cml.defaultnominator.config.database.common.DatabaseConfigurationPropertiesNames.*;

@Configuration
@EnableJpaRepositories(
        basePackages = NAMED_OBJECTS_REPOSITORY_PACKAGE,
        entityManagerFactoryRef = NAMED_OBJECTS_DATABASE_ENTITY_MANAGER_FACTORY,
        transactionManagerRef = NAMED_OBJECTS_DATABASE_TRANSACTION_MANAGER)
public class NamedObjectsDatabaseConfig {
    private DataSourceProperties properties;

    @Primary
    @Bean(NAMED_OBJECTS_DATABASE_PROPERTY)
    @ConfigurationProperties(prefix = NAMED_OBJECTS_PROPERTY_PREFIX)
    public DataSourceProperties getRemoteDatabaseProperties() {
        properties = new DataSourceProperties();
        return properties;
    }

    @Primary
    @Bean(NAMED_OBJECTS_DATABASE_DATA_SOURCE)
    public DataSource remoteDatabaseDatasource(
            @Qualifier(NAMED_OBJECTS_DATABASE_PROPERTY) DataSourceProperties properties
    ) {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(properties.getDriver());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setUrl(properties.getPrefix() + properties.getUrl());

        return dataSource;
    }

    @Primary
    @Bean(NAMED_OBJECTS_DATABASE_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(remoteDatabaseDatasource(getRemoteDatabaseProperties()));
        em.setPackagesToScan(NAMED_OBJECTS_PACKAGES_TO_SCAN.split(","));
        em.setPersistenceUnitName(NAMED_OBJECTS_PERSISTENCE_UNIT_NAME);

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> emProperties = new HashMap<>();
        emProperties.put("hibernate.hbm2ddl.auto", "update");
        emProperties.put("hibernate.dialect", properties.getDialect());
        em.setJpaPropertyMap(emProperties);

        return em;
    }

    @Primary
    @Bean(NAMED_OBJECTS_DATABASE_TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager().getObject());
        return transactionManager;
    }
}
