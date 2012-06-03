/*
 * Copyright 2012 Athens Team
 * 
 * This file to you under the Apache License, version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.eincs.athens.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jung-Haeng Lee
 */
public class MysqlHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(MysqlHandler.class);
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void insert(String ipAddr, String method, String path, String cause, long expiryTime) {
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/olympus?characterEncoding=UTF8";
		String driver = "com.mysql.jdbc.Driver";
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "olympus", "olympus");
			
			Statement st = conn.createStatement();
			String sql = String.format("REPLACE athens_block ( target, method, path, cause, expiry_time, created_time )" +
					"VALUES( '%s', '%s', '%s', '%s', '%s', '%s' );", ipAddr, method, path, cause
					, format.format(new Date(expiryTime)), format.format(new Date(System.currentTimeMillis())));
			System.out.println(sql);
			st.executeUpdate(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}
	
	
	public static void remove(String ipAddr, String method, String path) {
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/olympus?characterEncoding=UTF8";
		String driver = "com.mysql.jdbc.Driver";
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "olympus", "olympus");
			
			Statement st = conn.createStatement();
			String sql = String
					.format("DELETE FROM athens_block WHERE target = '%s' AND method ='%s' AND path ='%s';",
							ipAddr, method, path);
			System.out.println(sql);
			st.executeUpdate(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}
	
	public static void clean() {
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/olympus?characterEncoding=UTF8";
		String driver = "com.mysql.jdbc.Driver";
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "olympus", "olympus");
			
			Statement st = conn.createStatement();
			String sql = String
					.format("DELETE FROM athens_block WHERE expiry_time < '%s';",
							format.format(new Date(System.currentTimeMillis())));
			System.out.println(sql);
			st.executeUpdate(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}
}
