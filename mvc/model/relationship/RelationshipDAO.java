package mvc.model.relationship;

import java.sql.ResultSet;
import java.sql.SQLException;

import utils.DBConnection;
import utils.query.QueryBuilder;
import utils.tables.RelationshipFields;

/**
 * @packageName : mvc.model.relationship
 * @fileNmae	: RelationshipDAO.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Relationship table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class RelationshipDAO {
	private DBConnection dbConnection = new DBConnection();
	
	// 마지막으로 입력한 relationship의 ID를 반환하는 메소드
	private int getLastInsertedRelationshipID() {
		int id = 0;
		ResultSet rs = null;

		this.dbConnection.connect(QueryBuilder.getLastInsertedRelationshipID());
		try {
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(RelationshipFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException RelationshipDAO:getLastInsertedRelationshipID");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// 입력받은 relationship가 있으면 그 relationship의 ID를 반환하는 메소드 (없으면 0 반환)
	private int getRelationshipIfRelationshipInRelationshipTable(String relationship) {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getRelationshipIfRelationshipInRelationshipTable());
		try {
			this.dbConnection.getPreparedStatement().setString(1, relationship);
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(RelationshipFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException RelationshipDAO:getRelationshipIfRelationshipInRelationshipTable");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// 데이터베이스에 새 relationship를 삽입하고 ID를 반환하거나, 데이터베이스에 이미 relationship가 있는 경우 해당 relationship의 ID를 반환하는 메서드
	public int insertRelationship(String relationship) {
		int potentialRelationshipID = this.getRelationshipIfRelationshipInRelationshipTable(relationship);
		if (potentialRelationshipID != 0) {
			return potentialRelationshipID;
		}
		boolean didThrowError = false;

		this.dbConnection.connect(QueryBuilder.insertRelationship());
		try {
			this.dbConnection.getPreparedStatement().setString(1, relationship);
			this.dbConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException RelationshipDAO:insertRelationship");
			didThrowError = true;
		} finally {
			this.dbConnection.disconnect();
		}
		if (didThrowError) {
			return 0;
		}
		return this.getLastInsertedRelationshipID();
	}
}
