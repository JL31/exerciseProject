package com.jl31.exercise.tools;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.text.MessageFormat;

/**
 * Defines the data source configuration to establish a JDBC to a database
 */
public class DataSourceConfig {

    /**
     * Defines the database credentials to help establishing a JDBC connector
     * @param localEnvironment enables to know whether working on a local environment or not
     * @return database credentials to establish a JDBC connector
     */
    private static DatabaseCredentials getDatabaseCredentials (boolean localEnvironment) {
        // For now credentials are handled through environment variable but to be noted that this is not a safe option
        String host = System.getenv("POSTGRESQL_HOST");
        String port = System.getenv("POSTGRESQL_PORT");
        String databaseName = System.getenv("POSTGRESQL_DATABASE_NAME");
        String user = System.getenv("POSTGRESQL_DATABASE_USER");
        String password = System.getenv("POSTGRESQL_DATABASE_PASSWORD");

        return new DatabaseCredentials(host, port, databaseName, user, password);
    }

    /**
     * Defines the data source configuration from provided credentials
     * @param database_credentials the credentials to configure the data source
     * @return the data source configuration
     */
    private static HikariConfig getHikariConfig(DatabaseCredentials database_credentials) {
        final String url = MessageFormat.format(
            "jdbc:postgresql://{0}:{1}/{2}",
            database_credentials.host(),
            database_credentials.port(),
            database_credentials.databaseName()
        );

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url);
        config.setUsername(database_credentials.databaseUser());
        config.setPassword(database_credentials.databasePassword());
        config.setDriverClassName("org.postgresql.Driver");

        return config;
    }

    /**
     * Defines the data source from the credentials
     * @return the data source
     */
    public DataSource dataSource() {
        //  String currentEnvironment = System.getenv("APP_ENV");
        //  boolean isLocalEnvironment = currentEnvironment.equals("local");
        boolean isLocalEnvironment = true;  // temporary

        DatabaseCredentials database_credentials = getDatabaseCredentials(isLocalEnvironment);

        HikariConfig hikariConfig = getHikariConfig(database_credentials);

        return new HikariDataSource(hikariConfig);
    }
}
