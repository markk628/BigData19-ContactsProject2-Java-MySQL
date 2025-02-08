package utils.tables;

/**
 * @packageName : utils.tables
 * @fileNmae	: RelationshipFields.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Relationship table enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public enum RelationshipFields {
	ID("relationship_id"),
	RELATIONSHIP("relationship");
	
	private final String field;
	
	private RelationshipFields(String field) {
		this.field = field;
	}
	
	public static String tableName() {
		return "relationship";
	}
	
	@Override
	public String toString() {
		return this.field;
	}

}
