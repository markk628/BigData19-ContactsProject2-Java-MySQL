package utils.tables;

/**
 * @packageName : utils.tables
 * @fileNmae	: CompanyFields.java
 * @author		: mark
 * @date		: 2025.02.04
 * @description : Company table enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.02.04		MARK KIM		FIRST CREATED
 */
public enum CompanyFields {
	ID("company_id"),
	COMPANY("company");

	private final String field;

	private CompanyFields(String field) {
		this.field = field;
	}

	public static String tableName() {
		return "company";
	}

	@Override
	public String toString() {
		return this.field;
	}
}
