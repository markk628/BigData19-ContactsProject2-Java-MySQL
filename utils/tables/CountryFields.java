package utils.tables;

/**
 * @packageName : utils.tables
 * @fileNmae	: CountryFields.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Country table enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public enum CountryFields {
	ID("country_id"),
	COUNTRY("country");
	
private final String field;
	
	private CountryFields(String field) {
		this.field = field;
	}

	public CountryFields field(String field) {
		return null;
	}

	public static String tableName() {
		return "country";
	}
	
	@Override
	public String toString() {
		return this.field;
	}
	
	public String toStringForJob() {
		return "job_" + this.field;
	}
}
