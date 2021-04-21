package com.plex.configuration.db;

import com.plex.configuration.dialect.SQLiteDialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.plex.repository.sqlite",
        entityManagerFactoryRef = "sqliteEntityManagerFactory",
        transactionManagerRef = "sqliteTransactionManager"
)
public class SqliteConfiguration {

    @Bean(name = "sqliteDataSource")
    @ConfigurationProperties(prefix = "spring.sqlite-datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqliteEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier(value = "sqliteDataSource")
                                                                               DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .properties(Map.of("hibernate.dialect", SQLiteDialect.class.getName()))
                .packages("com.plex.model.sqlite")
                .persistenceUnit("sqlite")
                .build();
    }

    @Bean(name = "sqliteTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier(value = "sqliteEntityManagerFactory")
                                                                 EntityManagerFactory sqliteEntityManagerFactory) {
        return new JpaTransactionManager(sqliteEntityManagerFactory);
    }
}
