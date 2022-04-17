package com.myapp.repository;

import com.myapp.config.LiquibaseConfiguration;
import java.util.function.Supplier;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableConfigurationProperties({ LiquibaseProperties.class, R2dbcProperties.class })
public class LiquibaseConfig {

    private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    public static SpringLiquibase createSimpleLiquibase(LiquibaseProperties liquibaseProperties, R2dbcProperties dataSourceProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
        return liquibase;
    }

    private static DataSource createNewDataSource(LiquibaseProperties liquibaseProperties, R2dbcProperties dataSourceProperties) {
        String user = getProperty(liquibaseProperties::getUser, dataSourceProperties::getUsername);
        String password = getProperty(liquibaseProperties::getPassword, dataSourceProperties::getPassword);
        return DataSourceBuilder.create().url(liquibaseProperties.getUrl()).username(user).password(password).build();
    }

    private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
        String value = property.get();
        return (value != null) ? value : defaultValue.get();
    }

    @Bean
    public SpringLiquibase liquibase(LiquibaseProperties liquibaseProperties, R2dbcProperties dataSourceProperties) {
        SpringLiquibase liquibase = createSimpleLiquibase(liquibaseProperties, dataSourceProperties);
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        log.debug("Configuring Liquibase");

        return liquibase;
    }
}
