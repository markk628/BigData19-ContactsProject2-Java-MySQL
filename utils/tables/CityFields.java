package utils.tables;

/**
 * @packageName : utils.tables
 * @fileNmae	: CityFields.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : City table enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public enum CityFields {
	ID("city_id"),
	CITY("city"),
	STATE("state_id");
	
	private final String field;
	
	private CityFields(String field) {
		this.field = field;
	}

	public static String tableName() {
		return "city";
	}
	
	@Override
	public String toString() {
		return this.field;
	}
	
	public String toStringForJob() {
		return "job_" + this.field;
	}
}
