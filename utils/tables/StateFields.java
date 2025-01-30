package utils.tables;

/**
 * @packageName : utils.tables
 * @fileNmae	: StateFields.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : State table enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public enum StateFields {
	ID("state_id"),
	STATE("state"),
	COUNTRY("country_id");
	
	private final String field;
	
	private StateFields(String field) {
		this.field = field;
	}

	public StateFields field(String field) {
		switch (field.toLowerCase()) {
		case "city_id":
			return ID;
		case "state":
			return STATE;
		default:
			return COUNTRY;
		}
	}

	public static String tableName() {
		return "state";
	}
	
	@Override
	public String toString() {
		return this.field;
	}
}
