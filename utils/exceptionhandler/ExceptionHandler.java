package utils.exceptionhandler;

import java.util.Scanner;

/**
 * @packageName : utils.exceptionhandler
 * @fileNmae	: ExceptionHandler.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : 사용자 입력의 예외처리 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class ExceptionHandler {
	private Scanner scanner;

	public ExceptionHandler(Scanner scanner) {
		this.scanner = scanner;
	}

	// 입력받은 문자열이 비어있으면 Exceptiond을 던지고 아니면 반환하는 method
	private String returnInputIfValid(Input option) throws Exception {
		String field = this.scanner.nextLine();
		if (field.isBlank() || field.isEmpty()) {
			throw new Exception(option.toString() + " can't be empty");
		}
		return field;
	}

	// 입력받은 번호의 길이가 11이 않이거나 문자가 포함되있으면 Exception을 던지고 아니면 반환하는 method
	private String returnNumberIfValid(String number) throws Exception {
		if ((number.length() != 11) || (!number.matches("\\d+"))) {
			throw new Exception("Enter 11 digits");
		}

		return number;
	}

	// 입력받은 답장이 "yes", "no", "y", "n"중 하나가 아니면 Exception을 던지고, 그중 하나이면 반환하는 method
	private String returnYesOrNoIfValid(String answer) throws Exception {
		switch (answer.toLowerCase()) {
		case "yes", "no", "y", "n":
			return answer;
		default:
			throw new Exception("Enter yes/no/y/n");
		}
	}

	// Input enum case option을 이용해 어떤 method를 호출한지 정하고 호출한 method의 값을 반환하는 method
	public String handleInputException(Input option) {
		String input;
		try {
			input = this.returnInputIfValid(option);
			if (input.toLowerCase().equals("back")) {
				return input;
			}
			switch (option) {
			case NAME, ADDRESS, RELATIONSHIP, OPTION:
				return input;
			case NUMBER:
				return this.returnNumberIfValid(input);
			default:
				return this.returnYesOrNoIfValid(input);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.print("Enter " + option.toString().toLowerCase() + ": ");
			return this.handleInputException(option);
		}
	}

	// 입력받은 정수 문자열을 정수로 변환하고 반환하는 method
	public int handleParseIntException(String number) {
		try {
			return Integer.parseInt(number != null ? number : this.scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.print("enter a NUMBER: ");
			return this.handleParseIntException(null);
		}
	}
}
