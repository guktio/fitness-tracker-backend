package com.fitness.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseLogger {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLogger.class);
    private final DataSource dataSource;

    public DatabaseLogger(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            String url = connection.getMetaData().getURL();
            String version = connection.getMetaData().getDatabaseProductVersion();
            String username = connection.getMetaData().getUserName();
            String databaseProductName = connection.getMetaData().getDatabaseProductName();
            log.info("==================================================");
            log.info("Connected to database!");
            log.info("Database: {}", databaseProductName);
            log.info("Version: {}", version);
            log.info("URL: {}", url);
            log.info("User: {}", username);
            log.info("==================================================");

        } catch (SQLException e) {
            log.error("Failed to retrieve database connection metadata", e);
        }
    }
}
