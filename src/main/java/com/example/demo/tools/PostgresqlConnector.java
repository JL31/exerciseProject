package com.example.demo.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.text.MessageFormat;

// public class PostgresqlConnector implements IDatabaseConnector {  // pas retenu pour le moment, à réfléchir (potentiel problème de typage)
public class PostgresqlConnector {
    private static DatabaseCredentials getDatabaseCredentials (boolean localEnvironment) {
        // For now credentials are handled through environment variable but to be noted that this is not a safe option
        String host = System.getenv("POSTGRESQL_HOST");
        String port = System.getenv("POSTGRESQL_PORT");
        String databaseName = System.getenv("POSTGRESQL_DATABASE_NAME");
        String user = System.getenv("POSTGRESQL_DATABASE_USER");
        String password = System.getenv("POSTGRESQL_DATABASE_PASSWORD");

        return new DatabaseCredentials(host, port, databaseName, user, password);
    }

    public Connection getConnection() throws SQLException {
        //  String currentEnvironment = System.getenv("APP_ENV");
        //  boolean isLocalEnvironment = currentEnvironment.equals("local");
        boolean isLocalEnvironment = true;  // temporary
        DatabaseCredentials database_credentials = getDatabaseCredentials(isLocalEnvironment);

        final String url = MessageFormat.format(
            "jdbc:postgresql://{0}:{1}/{2}",
            database_credentials.host(),
            database_credentials.port(),
            database_credentials.databaseName()
        );

        return DriverManager.getConnection(
            url,
            database_credentials.databaseUser(),
            database_credentials.databasePassword()
        );
    }
}
