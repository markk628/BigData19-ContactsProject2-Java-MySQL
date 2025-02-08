package mvc.model.profession;

import java.sql.ResultSet;
import java.sql.SQLException;

import utils.DBConnection;
import utils.query.QueryBuilder;
import utils.tables.ProfessionFields;

/**
 * @packageName : mvc.model.profession
 * @fileNmae	: ProfessionDAO.java
 * @author		: mark
 * @date		: 2025.02.04
 * @description : Controls the profession table
 * 				  profession table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.02.04		MARK KIM		FIRST CREATED
 */
public class ProfessionDAO {
	private DBConnection dbConnection = new DBConnection();
	
	// Returns the ID of the last inserted profession (if the profession table is empty will return 0)
	// 마지막으로 입력한 profession의 ID를 반환하는 메소드 (profession table이 비어있으면 0 반환)
	private int getLastInsertedProfessionID() {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getLastInsertedProfessionID());
		try {
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(ProfessionFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException ProfessionDAO:getLastInsertedProfessionID");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// Returns the id of the passed in profession (if it's not in the table will return 0)
	// 입력받은 profession가 있으면 그 profession의 ID를 반환하는 메소드 (없으면 0 반환)
	private int getProfessionIfProfessionInProfessionTable(String profession) {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getProfessionIfProfessionInProfessionTable());
		try {
			this.dbConnection.getPreparedStatement().setString(1, profession);
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(ProfessionFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException ProfessionDAO:getProfessionIfProfessionInProfessionTable");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// Inserts the passed in profession and returns the ID if it's not in the database (returns the ID if it is in the database)
	// 데이터베이스에 새 profession를 삽입하고 ID를 반환하거나, 데이터베이스에 이미 profession가 있는 경우 해당 profession의 ID를 반환하는 메서드
	public int insertAndOrGetProfession(String profession) {
		int professionID = this.getProfessionIfProfessionInProfessionTable(profession);
		if (professionID != 0) {
			return professionID;
		}
		boolean didThrowError = false;
		
		this.dbConnection.connect(QueryBuilder.insertProfession());
		try {
			this.dbConnection.getPreparedStatement().setString(1, profession);
			this.dbConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException ProfessionDAO:insertAndOrGetProfession");
			didThrowError = true;
		} finally {
			this.dbConnection.disconnect();
		}
		if (didThrowError) {
			return 0;
		}
		return this.getLastInsertedProfessionID();
	}
}
