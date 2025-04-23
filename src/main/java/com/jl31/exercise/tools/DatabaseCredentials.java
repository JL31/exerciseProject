package com.jl31.exercise.tools;

/**
 * Records database credentials
 * @param host
 * @param port
 * @param databaseName
 * @param databaseUser
 * @param databasePassword
 */
public record DatabaseCredentials (
    String host,
    String port,
    String databaseName,
    String databaseUser,
    String databasePassword
) {}
