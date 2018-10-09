package com.zacheryharley.java.exchanger.exchange;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeMain {
	
	private static Logger logger = LoggerFactory.getLogger(ExchangeMain.class);
	private static Configurations configs = new Configurations();
	private static Configuration swappingEngineConfig;
	
	public static void main(String[] args) throws ConfigurationException {
		logger.info("Exchange starting");
		logger.info("Loading config from 'swappingEngine.conf'");
		swappingEngineConfig = configs.properties(new File("swappingEngine.conf"));
		
		//Load the config data for the swapping engine
		String dbhost, dbuser, dbpass, dbname;
		int dbport;
		
		dbhost = swappingEngineConfig.getString("database.host");
		dbname = swappingEngineConfig.getString("database.name");
		dbuser = swappingEngineConfig.getString("database.user");
		dbpass = swappingEngineConfig.getString("database.pass");
		dbport = swappingEngineConfig.getInteger("database.port", 0);
		
		DatabaseExecutor x = new DatabaseExecutor(dbport, dbhost, dbuser, dbpass, dbname);
		x.executeSQLFromFile(new File("sql/resetDatabase.sql"));
	}
}
