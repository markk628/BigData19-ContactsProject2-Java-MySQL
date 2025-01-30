package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @packageName : utils
 * @fileNmae	: DBConnection.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : JDBC를 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class DBConnection {
	private Connection connection 				= null;
	private PreparedStatement preparedStatement = null;
	private Statement statement 			 	= null;
	private ResultSet resultSet					= null;
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public PreparedStatement getPreparedStatement() {
		return this.preparedStatement;
	}
	
	public Statement getStatement() {
		return this.statement;
	}
	
	// JDBC와 연결후 받은 query로 PreparedStatement 준비시키는 메소드
	public void connect(String query) {
		try {
			Class.forName(Utils.driverLocation());
			this.connection = DriverManager.getConnection(Utils.url(), Utils.id(), Utils.pw());
			this.connection.setAutoCommit(false);
			this.preparedStatement = this.connection.prepareStatement(query);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// JDBC와 연결후 받은 queries들을 statement의 batch에 삽입시킨후 실행하는 메소드
	public void connectAndExecuteBatches(ArrayList<String> queries) {
		int[] flags;
		try {
			Class.forName(Utils.driverLocation());
			this.connection = DriverManager.getConnection(Utils.url(), Utils.id(), Utils.pw());
			this.connection.setAutoCommit(false);
			this.statement = this.connection.createStatement();
			for (String query: queries) {
				this.statement.addBatch(query);
			}
			flags = this.statement.executeBatch();
			for (int flag: flags) {
				if(flag >= 0) {
					this.connection.commit();
				} else {
					this.connection.rollback();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
	}
	
	// preparedStatement를 실행하는 메소드 (select)
	public ResultSet executeQuery() {
		try {
			this.resultSet = this.preparedStatement.executeQuery();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.resultSet;
	}
	
	// preparedStatement를 실행하는 메소드 (insert, update, delete)
	public void executeUpdate() {
		try {
			if(this.preparedStatement.executeUpdate() == 1) {
				this.connection.commit();
			} else {
				this.connection.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// JDBC와 연결을 끊는 메소드
	public void disconnect() {
		if (this.resultSet != null) {
			try {
				this.resultSet.close();
				this.resultSet = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (this.preparedStatement != null) {
			try {
				this.preparedStatement.close();
				this.preparedStatement = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (this.statement != null) {
			try {
				this.statement.close();
				this.statement = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (this.connection != null) {
			try {
				this.connection.close();
				this.connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
