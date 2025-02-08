package utils.tables;

/**
 * @packageName : utils.tables
 * @fileNmae	: JobFields.java
 * @author		: mark
 * @date		: 2025.02.04
 * @description : Job table enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.02.04		MARK KIM		FIRST CREATED
 */
public enum JobFields {
	ID("job_id"),
	PROFESSION("profession_id"),
	COMPANY("company_id"),
	ADDRESS("work_address_id"),
	JOBADDRESS("job_address");
	
	private final String field;
	
	private JobFields(String field) {
		this.field = field;
	}

	public static String tableName() {
		return "job";
	}
	
	@Override
	public String toString() {
		return this.field;
	}
}
