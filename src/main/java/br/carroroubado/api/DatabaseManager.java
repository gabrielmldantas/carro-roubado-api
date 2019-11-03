package br.carroroubado.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

	public static Connection getConnection() throws IOException, SQLException {
		Properties databaseProperties = new Properties();
		databaseProperties.load(DatabaseManager.class.getResourceAsStream("/database.properties"));

		return DriverManager.getConnection(
				String.format("jdbc:postgresql://%s:%s/%s",
						databaseProperties.getProperty("host"),
						databaseProperties.getProperty("port"),
						databaseProperties.getProperty("database")
				),
				databaseProperties.getProperty("user"),
				databaseProperties.getProperty("password"));
	}
}
