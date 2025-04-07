package com.example.demo.tools;

public record DatabaseCredentials (
    String host,
    String port,
    String databaseName,
    String databaseUser,
    String databasePassword
) {}
