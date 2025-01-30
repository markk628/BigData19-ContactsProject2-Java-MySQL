package mvc.model.address;

import java.sql.ResultSet;
import java.sql.SQLException;

import mvc.model.city.CityDAO;
import mvc.model.country.CountryDAO;
import mvc.model.state.StateDAO;
import utils.DBConnection;
import utils.query.QueryBuilder;
import utils.tables.AddressFields;

/**
 * @packageName : mvc.model.address
 * @fileNmae	: AddressDAO.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Address table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class AddressDAO {
	private CountryDAO countryDAO = new CountryDAO();
	private StateDAO stateDAO = new StateDAO();
	private CityDAO cityDAO = new CityDAO();
	private DBConnection dbConnection = new DBConnection();
	
	// 마지막으로 입력한 address의 ID를 반환하는 메소드 (address table이 비어있으면 0 반환)
	private int getLastInsertedAddressID() {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getLastInsertedAddressID());
		try {
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(AddressFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException AddressDAO:getLastInsertedAddressID");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// 입력받은 address가 있으면 그 address의 ID를 반환하는 메소드 (없으면 0 반환)
	private int getAddressIfAddressInAddressTable(String address, int cityID) {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getAddressIfAddressInAddressTable());
		try {
			this.dbConnection.getPreparedStatement().setString(1, address);
			this.dbConnection.getPreparedStatement().setInt(2, cityID);
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(AddressFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException AddressDAO:getAddressIfAddressInAddressTable");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// 데이터베이스에 새 address를 삽입하고 ID를 반환하거나, 데이터베이스에 이미 address가 있는 경우 해당 address의 ID를 반환하는 메서드 
	public int insertAddress(String address, String city, String state, String country, String zip_code) {
		int countryID = this.countryDAO.insertAndOrGetCountry(country);
		int stateID = this.stateDAO.insertAndOrGetState(state, countryID);
		int cityID = this.cityDAO.insertAndOrGetCity(city, stateID);
		int potentialAddressID = this.getAddressIfAddressInAddressTable(address, cityID);
		if (potentialAddressID != 0) {
			return potentialAddressID;
		}
		boolean didThrowError = false;

		this.dbConnection.connect(QueryBuilder.insertAddress());
		try {
			this.dbConnection.getPreparedStatement().setString(1, address);
			this.dbConnection.getPreparedStatement().setInt(2, cityID);
			this.dbConnection.getPreparedStatement().setString(3, zip_code);
			this.dbConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException AddressDAO:insertAddress");
			didThrowError = true;
		} finally {
			this.dbConnection.disconnect();
		}
		if (didThrowError) {
			return 0;
		}
		return this.getLastInsertedAddressID();
	}
}
