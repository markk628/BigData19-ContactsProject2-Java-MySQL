package mvc.model.state;

import java.sql.ResultSet;
import java.sql.SQLException;

import utils.DBConnection;
import utils.query.QueryBuilder;
import utils.tables.StateFields;

/**
 * @packageName : mvc.model.state
 * @fileNmae	: StateDAO.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Controls the state table
 * 				  state table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class StateDAO {
	private DBConnection dbConnection = new DBConnection();
	
	// Returns the ID of the last inserted state (if the state table is empty will return 0)
	// 마지막으로 입력한 state의 ID를 반환하는 메소드
	private int getLastInsertedStateID() {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getLastInsertedStateID());
		try {
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(StateFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException StateDAO:getLastInsertedStateID");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// Returns the id of the passed in state (if it's not in the table will return 0)
	// 입력받은 state가 있으면 그 state의 ID를 반환하는 메소드 (없으면 0 반환)
	private int getStateIfStateInStateTable(String state, int countryID) {
		int id = 0;
		ResultSet rs = null;
		
		this.dbConnection.connect(QueryBuilder.getStateIfStateInStateTable());
		try {
			this.dbConnection.getPreparedStatement().setString(1, state);
			this.dbConnection.getPreparedStatement().setInt(2, countryID);
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(StateFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException StateDAO:getStateIfStateInStateTable");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}
	
	// Inserts the passed in state and returns the ID if it's not in the database (returns the ID if it is in the database)
	// 데이터베이스에 새 state를 삽입하고 ID를 반환하거나, 데이터베이스에 이미 state가 있는 경우 해당 state의 ID를 반환하는 메서드
	public int insertAndOrGetState(String state, int countryID) {
		int stateID = this.getStateIfStateInStateTable(state, countryID);
		if (stateID != 0) {
			return stateID;
		}
		boolean didThrowError = false;

		this.dbConnection.connect(QueryBuilder.insertState());
		try {
			this.dbConnection.getPreparedStatement().setString(1, state);
			this.dbConnection.getPreparedStatement().setInt(2, countryID);
			this.dbConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException StateDAO:insertAndOrGetState");
			didThrowError = true;
		} finally {
			this.dbConnection.disconnect();
		}
		if (didThrowError) {
			return 0;
		}
		return this.getLastInsertedStateID();
	}
}
