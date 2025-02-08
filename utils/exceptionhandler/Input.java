package utils.exceptionhandler;

/**
 * @packageName : utils.exceptionhandler
 * @fileNmae	: Input.java
 * @author		: mark
 * @date		: 2025.01.28
 * @description : Enum that indicates what the user will be inputing
 * 				  무엇을 입력받을지 여부하는 enum
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.28		MARK KIM		FIRST CREATED
 */
public enum Input {
	NAME("Name"),
	NUMBER("Number"),
	ADDRESS("Address"),
	RELATIONSHIP("Relationship"),
	OPTION("Option"),
	YESORNO("Yes or no");

	private final String name;

	private Input(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
