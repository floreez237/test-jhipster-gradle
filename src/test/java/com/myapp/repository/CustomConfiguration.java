package com.myapp.repository;

import com.myapp.config.DatabaseConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestConfiguration
@ComponentScan(basePackages = "com.myapp.repository")
@Import({ DatabaseConfiguration.class, LiquibaseConfig.class })
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:config/application.yml")
public class CustomConfiguration {}
