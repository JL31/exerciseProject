package com.example.demo.tools;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.text.MessageFormat;

public class DataSourceConfig {

    private static DatabaseCredentials getDatabaseCredentials (boolean localEnvironment) {
        // For now credentials are handled through environment variable but to be noted that this is not a safe option
        String host = System.getenv("POSTGRESQL_HOST");
        String port = System.getenv("POSTGRESQL_PORT");
        String databaseName = System.getenv("POSTGRESQL_DATABASE_NAME");
        String user = System.getenv("POSTGRESQL_DATABASE_USER");
        String password = System.getenv("POSTGRESQL_DATABASE_PASSWORD");

        return new DatabaseCredentials(host, port, databaseName, user, password);
    }

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

    public DataSource dataSource() {
        //  String currentEnvironment = System.getenv("APP_ENV");
        //  boolean isLocalEnvironment = currentEnvironment.equals("local");
        boolean isLocalEnvironment = true;  // temporary

        DatabaseCredentials database_credentials = getDatabaseCredentials(isLocalEnvironment);

        HikariConfig hikariConfig = getHikariConfig(database_credentials);

        return new HikariDataSource(hikariConfig);
    }
}
