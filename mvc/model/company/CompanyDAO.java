package mvc.model.company;

import java.sql.ResultSet;
import java.sql.SQLException;

import utils.DBConnection;
import utils.query.QueryBuilder;
import utils.tables.CompanyFields;

/**
 * @packageName : mvc.model.company
 * @fileNmae	: CompanyDAO.java
 * @author		: mark
 * @date		: 2025.02.04
 * @description : Controls the company table 
 * 				  company table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.02.04		MARK KIM		FIRST CREATED
 */
public class CompanyDAO {
	private DBConnection dbConnection = new DBConnection();
	
	// Returns the ID of the last inserted company (if the company table is empty will return 0)
	// 마지막으로 입력한 company의 ID를 반환하는 메소드 (company table이 비어있으면 0 반환)
	private int getLastInsertedCompanyID() {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getLastInsertedCompanyID());
		try {
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(CompanyFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException CompanyDAO:getLastInsertedCompanyID");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// Returns the id of the passed in company (if it's not in the table will return 0)
	// 입력받은 company가 있으면 그 company의 ID를 반환하는 메소드 (없으면 0 반환)
	private int getCompanyIfCompanyInCompanyTable(String company) {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getCompanyIfCompanyInCompanyTable());
		try {
			this.dbConnection.getPreparedStatement().setString(1, company);
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(CompanyFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException CompanyDAO:getCompanyIfCompanyInCompanyTable");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// Inserts the passed in company and returns the ID if it's not in the database (returns the ID if it is in the database)
	// 데이터베이스에 새 company를 삽입하고 ID를 반환하거나, 데이터베이스에 이미 company가 있는 경우 해당 company의 ID를 반환하는 메서드
	public int insertAndOrGetCompany(String company) {
		int companyID = this.getCompanyIfCompanyInCompanyTable(company);
		if (companyID != 0) {
			return companyID;
		}
		boolean didThrowError = false;
		
		this.dbConnection.connect(QueryBuilder.insertCompany());
		try {
			this.dbConnection.getPreparedStatement().setString(1, company);
			this.dbConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException CompanyDAO:insertAndOrGetCompany");
			didThrowError = true;
		} finally {
			this.dbConnection.disconnect();
		}
		if (didThrowError) {
			return 0;
		}
		return this.getLastInsertedCompanyID();
	}
}
