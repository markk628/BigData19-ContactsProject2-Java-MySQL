package utils.tables;

/**
 * @packageName : utils.tables
 * @fileNmae	: ProfessionFields.java
 * @author		: mark
 * @date		: 2025.02.04
 * @description : Profession table enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.02.04		MARK KIM		FIRST CREATED
 */
public enum ProfessionFields {
	ID("profession_id"),
	PROFESSION("profession");
	
	private final String field;
	
	private ProfessionFields(String field) {
		this.field = field;
	}

	public static String tableName() {
		return "profession";
	}
	
	@Override
	public String toString() {
		return this.field;
	}
}
