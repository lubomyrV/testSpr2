package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class DatabaseInitConfig {
	
	@Autowired Environment env;
	
	@Autowired DataSource dataSource;
	
	@PostConstruct
	public void initialize(){
		Connection connection = null;
		Statement statement = null;
		try {
			
			String aSQLScriptFilePath = env.getProperty("aSQLScriptFilePath");
			
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			
			String databaseName = env.getProperty("databaseName");
			
			ResultSet result = statement.executeQuery(
					"SELECT * " + 
					"FROM information_schema.tables " + 
					"WHERE table_schema = '"+databaseName+"' " + 
					"    AND table_name = 'user' " + 
					"LIMIT 1;");
            if(!result.next()) {
            	System.out.println("Initialize database");
            	
            	// Initialize object for ScripRunner
    			ScriptRunner sr = new ScriptRunner(connection, false, false);

    			// Give the input file to Reader
    			Reader reader = null;
    			try {
    				reader = new BufferedReader(new FileReader(aSQLScriptFilePath));
    			} catch (FileNotFoundException e) {
    				System.out.println("FileNotFoundException: "+ e.getMessage());
    				//e.printStackTrace();
    			}

    			// Exctute script
    			try {
    				sr.runScript(reader);
    			} catch (IOException e) {
    				System.out.println("IOException: "+ e.getMessage());
    				//e.printStackTrace();
    			}

            }

			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("SQLException: "+ e.getMessage());
			//e.printStackTrace();
		}
	}

}
