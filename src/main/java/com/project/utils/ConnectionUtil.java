package com.project.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Singleton utility for creating and retrieving database connection
 */

public class ConnectionUtil {
	
	private static ConnectionUtil cu = null;
	private static Properties prop;
	String url;
	String user;
	String pass;
	Connection conn;
	
	/**
	 * This method should read in the "database.properties" file and load
	 * the values into the Properties variable
	 */
	private ConnectionUtil() {
		
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream is = classLoader.getResourceAsStream("database.properties");
		prop = new Properties();
		try {
			prop.load(is);
			url = (String) prop.getProperty("url");
			user = (String) prop.getProperty("usr");
			pass = (String) prop.getProperty("pswd");
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(this.url, this.user, this.pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized ConnectionUtil getConnectionUtil() {
		if(cu == null)
			cu = new ConnectionUtil();
		return cu;
	}
	
	/**
	 * This method should create and return a Connection object
	 * @return a Connection to the database
	 */
	public Connection getConnection() {
		// Hint: use the Properties variable to setup your Connection object
		return conn;
	}
}
