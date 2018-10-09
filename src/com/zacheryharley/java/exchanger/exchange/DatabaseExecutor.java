package com.zacheryharley.java.exchanger.exchange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseExecutor {
	private Connection connection;
	private static Logger logger = LoggerFactory.getLogger(DatabaseExecutor.class);
	
	
	/////////////////////////////////////////
	////////// Constructors /////////////////
	/////////////////////////////////////////
	
	public DatabaseExecutor() {
	}
	
	public DatabaseExecutor(int port, String host, String user, String password, String database) {
		this.connect(port, host, user, password, database);
	}
	
	//////////////////////////////////////////
	/////// Public methods ///////////////////
	//////////////////////////////////////////
	
	/**
	 * Connect to the database with the given parameters.
	 * @param port - The port the database is on
	 * @param host - The host where the database is hosted
	 * @param user - The username to login to the database
	 * @param password - The password to login to the database
	 * @param database - The name of the database to open
	 * @return True if the connection was successful, false if it failed
	 */
	public boolean connect(int port, String host, String user, String password, String database){
		try {
			String connectionStr = "jdbc:postgresql://"+host+":"+port+"/"+database;
			logger.info("Connecting to database: {}", connectionStr);
			connection = DriverManager.getConnection(connectionStr, user, password);
			logger.info("Connection successful");
			return true;
		} catch (SQLException e) {
			logger.error("Failed to connect to database", e);
			return false;
		}
	}
	
	/**
	 * Execute the SQL from a given file, update method is used and therefore all statements should have no
	 * return value.
	 * @param sqlFile
	 */
	public void executeSQLFromFile(File sqlFile) {
		logger.info("Executing SQL file: '{}'", sqlFile.getName());
		if(!sqlFile.exists()) {
			logger.error("The sql file: '{}' does not exsist!", sqlFile.getAbsolutePath());
			return;
		}
		
		BufferedReader reader = null;
		
		//Read the file and process each complete statement
		try {
			//Open the file ready to be read.
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile)));
			String line = null;
			String expression = "";
			
			//read the file line by line executing expressions
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.startsWith("--execute")) {
					executeSQLFromFile(new File(line.replace("--execute ", "")));
					continue;
				}
				
				if(line.endsWith(";")) {
					executeUpdateSQL(expression += line);
					expression = "";
				} else {
					expression += line;
				}
			}
			
		} catch (Exception e) {
			logger.error("Failed to execute SQL from file {}", sqlFile.getAbsolutePath(), e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				//Do nothing
			}
		}
	}
	
	/**
	 * Execute the given SQL expression as an update, and therefore should not be expected to
	 * return a value.
	 * @param sqlExpression - The expression to be run
	 * @throws SQLException - If the update failed to execute an exception with the reason is thrown. 
	 */
	public void executeUpdateSQL(String sqlExpression) throws SQLException {
		logger.debug("Executing query: '{}'", sqlExpression);
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlExpression);
	}
	
	
	
}
