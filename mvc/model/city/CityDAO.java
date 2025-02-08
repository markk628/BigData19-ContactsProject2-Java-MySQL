package mvc.model.city;

import java.sql.ResultSet;
import java.sql.SQLException;

import utils.DBConnection;
import utils.query.QueryBuilder;
import utils.tables.CityFields;

/**
 * @packageName : mvc.model.city
 * @fileNmae	: CityDAO.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Controls the city table 
 * 				  city table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class CityDAO {
	private DBConnection dbConnection = new DBConnection();

	// Returns the ID of the last inserted city (if the city table is empty will return 0)
	// 마지막으로 입력한 city의 ID를 반환하는 메소드
	private int getLastInsertedCityID() {
		int id = 0;
		ResultSet rs = null;

		this.dbConnection.connect(QueryBuilder.getLastInsertedCityID());
		try {
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(CityFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException CityDAO:getLastInsertedCityID");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}

	// Returns the id of the passed in city (if it's not in the table will return 0)
	// 입력받은 city가 있으면 그 city의 ID를 반환하는 메소드 (없으면 0 반환)
	private int getCityIfCityInCityTable(String city, int stateID) {
		int id = 0;
		ResultSet rs = null;

		this.dbConnection.connect(QueryBuilder.getCityIfCityInCityTable());
		try {
			this.dbConnection.getPreparedStatement().setString(1, city);
			this.dbConnection.getPreparedStatement().setInt(2, stateID);
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(CityFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException CityDAO:getCityIfCityInCityTable");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}

	// Inserts the passed in city and returns the ID if it's not in the database (returns the ID if it is in the database)
	// 데이터베이스에 새 city를 삽입하고 ID를 반환하거나, 데이터베이스에 이미 city가 있는 경우 해당 city의 ID를 반환하는 메서드
	public int insertAndOrGetCity(String city, int stateID) {
		int cityID = this.getCityIfCityInCityTable(city, stateID);
		if (cityID != 0) {
			return cityID;
		}
		boolean didThrowError = false;

		this.dbConnection.connect(QueryBuilder.insertCity());
		try {
			this.dbConnection.getPreparedStatement().setString(1, city);
			this.dbConnection.getPreparedStatement().setInt(2, stateID);
			this.dbConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException CityDAO:insertAndOrGetCity");
			didThrowError = true;
		} finally {
			this.dbConnection.disconnect();
		}
		if (didThrowError) {
			return 0;
		}
		return this.getLastInsertedCityID();
	}
}
