package mvc.model.job;

import java.sql.ResultSet;
import java.sql.SQLException;

import mvc.model.address.AddressDAO;
import mvc.model.address.AddressDTO;
import mvc.model.company.CompanyDAO;
import mvc.model.profession.ProfessionDAO;
import utils.DBConnection;
import utils.query.QueryBuilder;
import utils.tables.JobFields;

/**
 * @packageName : mvc.model.job
 * @fileNmae	: JobDAO.java
 * @author		: mark
 * @date		: 2025.02.04
 * @description : Controls the job table
 * 				  job table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.02.04		MARK KIM		FIRST CREATED
 */
public class JobDAO {
	private DBConnection dbConnection = new DBConnection();
	private AddressDAO addressDAO = new AddressDAO();
	private ProfessionDAO professionDAO = new ProfessionDAO();
	private CompanyDAO companyDAO = new CompanyDAO();

	// Returns the ID of the last inserted job (if the job table is empty will return 0)
	// 마지막으로 입력한 job의 ID를 반환하는 메소드 (job table이 비어있으면 0 반환)
	private int getLastInsertedJobID() {
		int id = 0;
		ResultSet rs = null;

		this.dbConnection.connect(QueryBuilder.getLastInsertedJobID());
		try {
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(JobFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException JobDAO:getLastInsertedJobID");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}

	// Returns the id of the passed in job (if it's not in the table will return 0)
	// 입력받은 job가 있으면 그 job의 ID를 반환하는 메소드 (없으면 0 반환)
	private int getJobIfJobInJobTable(int professionID, int companyID, int addressID) {
		int id = 0;
		ResultSet rs = null;

		this.dbConnection.connect(QueryBuilder.getJobIfJobInJobTable());
		try {
			this.dbConnection.getPreparedStatement().setInt(1, professionID);
			this.dbConnection.getPreparedStatement().setInt(2, companyID);
			this.dbConnection.getPreparedStatement().setInt(3, addressID);
			rs = this.dbConnection.executeQuery();
			if (rs.next()) {
				id = rs.getInt(JobFields.ID.toString());
			}
		} catch (SQLException e) {
			System.out.println("SQLException JobDAO:getJobIfJobInJobTable");
		} finally {
			this.dbConnection.disconnect();
		}
		return id;
	}

	// Inserts the passed in job and returns the ID if it's not in the database (returns the ID if it is in the database)
	// 데이터베이스에 새 job를 삽입하고 ID를 반환하거나, 데이터베이스에 이미 job가 있는 경우 해당 job의 ID를 반환하는 메서드
	public int insertAndOrGetJob(String profession, String company, AddressDTO address) {
		int professionID = this.professionDAO.insertAndOrGetProfession(profession);
		int companyID = this.companyDAO.insertAndOrGetCompany(company);
		int addressID = this.addressDAO.insertAndOrGetAddress(address);
		int jobID = this.getJobIfJobInJobTable(professionID, companyID, addressID);
		if (jobID != 0) {
			return jobID;
		}
		boolean didThrowError = false;

		this.dbConnection.connect(QueryBuilder.insertJob());
		try {
			this.dbConnection.getPreparedStatement().setInt(1, professionID);
			this.dbConnection.getPreparedStatement().setInt(2, companyID);
			this.dbConnection.getPreparedStatement().setInt(3, addressID);
			this.dbConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException JobDAO:insertAndOrGetJob");
			didThrowError = true;
		} finally {
			this.dbConnection.disconnect();
		}
		if (didThrowError) {
			return 0;
		}
		return this.getLastInsertedJobID();
	}
}
