package mvc.model.country;

import java.sql.ResultSet;
import java.sql.SQLException;

import utils.DBConnection;
import utils.query.QueryBuilder;
import utils.tables.CountryFields;

/**
 * @packageName : mvc.model.country
 * @fileNmae	: CountryDAO.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Controls the country table 
 * 				  country table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class CountryDAO {
	private DBConnection dbConnection = new DBConnection();
	
	// Returns the ID of the last inserted country (if the country table is empty will return 0)
	// 마지막으로 입력한 country의 ID를 반환하는 메소드
	private int getLastInsertedCountryID() {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getLastInsertedCountryID());
		try {
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(CountryFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException CountryDAO:getLastInsertedCountryID");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// Returns the id of the passed in country (if it's not in the table will return 0)
	// 입력받은 country가 있으면 그 country의 ID를 반환하는 메소드 (없으면 0 반환)
	private int getCountryIfCountryInCountryTable(String country) {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getCountryIfCountryInCountryTable());
		try {
			this.dbConnection.getPreparedStatement().setString(1, country);
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(CountryFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException CountryDAO:getCountryIfCountryInCountryTable");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// Inserts the passed in country and returns the ID if it's not in the database (returns the ID if it is in the database)
	// 데이터베이스에 새 country를 삽입하고 ID를 반환하거나, 데이터베이스에 이미 country가 있는 경우 해당 country의 ID를 반환하는 메서드
	public int insertAndOrGetCountry(String country) {
		int countryID = this.getCountryIfCountryInCountryTable(country);
		if (countryID != 0) {
			return countryID;
		}
		boolean didThrowError = false;
		
		this.dbConnection.connect(QueryBuilder.insertCountry());
		try {
			this.dbConnection.getPreparedStatement().setString(1, country);
			this.dbConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException CountryDAO:insertAndOrGetCountry");
			didThrowError = true;
		} finally {
			this.dbConnection.disconnect();
		}
		if (didThrowError) {
			return 0;
		}
		return this.getLastInsertedCountryID();
	}
}
