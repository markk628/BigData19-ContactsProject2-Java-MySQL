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
 * @description : Controls the address table 
 * 				  address table을 제어하는 클래스
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
	
	// Returns the ID of the last inserted address (if the address table is empty will return 0)
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
	
	// Returns the id of the passed in address (if it's not in the table will return 0)
	// 입력받은 address가 있으면 그 address의 ID를 반환하는 메소드 (없으면 0 반환)
	private int getAddressIfAddressInAddressTable(String address, int cityID, String zipCode) {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getAddressIfAddressInAddressTable());
		try {
			this.dbConnection.getPreparedStatement().setString(1, address);
			this.dbConnection.getPreparedStatement().setInt(2, cityID);
			this.dbConnection.getPreparedStatement().setString(3, zipCode);
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
	
	// Inserts the passed in address and returns the ID if it's not in the database (returns the ID if it is in the database)
	// 데이터베이스에 새 address를 삽입하고 ID를 반환하거나, 데이터베이스에 이미 address가 있는 경우 해당 address의 ID를 반환하는 메서드 
	public int insertAndOrGetAddress(AddressDTO address) {
		int countryID = this.countryDAO.insertAndOrGetCountry(address.getCountry());
		int stateID = this.stateDAO.insertAndOrGetState(address.getState(), countryID);
		int cityID = this.cityDAO.insertAndOrGetCity(address.getCity(), stateID);
		int addressID = this.getAddressIfAddressInAddressTable(address.getStreetAddress(), cityID, address.getZipCode());
		if (addressID != 0) {
			return addressID;
		}
		boolean didThrowError = false;

		this.dbConnection.connect(QueryBuilder.insertAddress());
		try {
			this.dbConnection.getPreparedStatement().setString(1, address.getStreetAddress());
			this.dbConnection.getPreparedStatement().setInt(2, cityID);
			this.dbConnection.getPreparedStatement().setString(3, address.getZipCode());
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
