package utils.tables;

/**
 * @packageName : utils.tables
 * @fileNmae	: AddressFields.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Address table enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public enum AddressFields {
	ID("address_id"),
	ADDRESS("address"),
	CITY("city_id"),
	ZIPCODE("zip_code");
	
	private final String field;
	
	private AddressFields(String field) {
		this.field = field;
	}

	public static String tableName() {
		return "address";
	}
	
	@Override
	public String toString() {
		return this.field;
	}
	
	public String toStringForJob() {
		return "job_" + this.field;
	}
}
