package mvc.controller;

import java.util.Scanner;

import service.Service;

/**
 * @packageName : mvc.controller
 * @fileNmae	: ContactsController.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Starts/stops the contacts program 
 * 				  Contacts프로그램을 실행하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class ContactsController {
	public static void main(String[] args) {
		String option;
		Scanner scanner = new Scanner(System.in);
		Service contacts = new Service(scanner);
		
		while (true) {
			System.out.println();
			System.out.println("============================");
			System.out.println("      Select an option");
			System.out.println("============================");
			System.out.println("1. create a contact");
			System.out.println("2. look up a contact");
			System.out.println("3. look up all contacts");
			System.out.println("4. update a contact");
			System.out.println("5. delete a contact");
			System.out.println("6. close contacts");
			System.out.print("Option: ");
			option = scanner.nextLine();
			if (option.isEmpty() || option.isBlank()) {
				continue;
			}
			System.out.println();
			switch (option) {
			case "1":
				contacts.insert();
				break;
			case "2":
				contacts.selectWithName();
				break;
			case "3":
				contacts.select();
				break;
			case "4":
				contacts.update();
				break;
			case "5":
				contacts.delete();
				break;
			case "6":
				contacts.close();
				return;
			default:
				System.out.println(option + " is not an option");
			}
		}
	}
}
