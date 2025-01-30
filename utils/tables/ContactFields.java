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
	RELATIONSHIP("relationship_id")	;
	private final String field;
	
	private ContactFields(String field) {
		this.field = field;
	}
	
	public ContactFields field(String field) {
		switch (field.toLowerCase()) {
		case "contact_id":
			return ID;
		case "contact_name":
			return NAME;
		case "contact_number":
			return NUMBER;
		case "address_id":
			return ADDRESS;
		default:
			return RELATIONSHIP;
		}
	}
	
	public static String tableName() {
		return "contact";
	}
	
	@Override
	public String toString() {
		return this.field;
	}
}
