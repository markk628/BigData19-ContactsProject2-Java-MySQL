package utils.tables;

/**
 * @packageName : utils.tables
 * @fileNmae	: ContactFields.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Contact table enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public enum ContactFields {
	ID("contact_id"),
	NAME("contact_name"),
	NUMBER("contact_number"),
	ADDRESS("address_id"),
	RELATIONSHIP("relationship_id"),
	NATIONALITY("nationality_id"),
	JOB("job_id");
	private final String field;
	
	private ContactFields(String field) {
		this.field = field;
	}
	
	public static String tableName() {
		return "contact";
	}
	
	public static String nationality() {
		return "nationality";
	}
	
	@Override
	public String toString() {
		return this.field;
	}
}
