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

	public CityFields field(String field) {
		switch (field.toLowerCase()) {
		case "city_id":
			return ID;
		case "city":
			return CITY;
		default:
			return STATE;
		}
	}

	public static String tableName() {
		return "city";
	}
	
	@Override
	public String toString() {
		return this.field;
	}
}
