package mvc.view;

import java.util.ArrayList;

import mvc.model.contact.ContactDTO;

/**
 * @packageName : mvc.view
 * @fileNmae	: ContactView.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Prints out contacts database's DML statement results
 * 				  contacts database제어 DML 결과를 출력하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class ContactView {
	
	// INSERT
	public void insert(ContactDTO contact) {
		if (contact != null) {
			System.out.println("Contact added");
			System.out.println(contact);
		} else {
			System.out.println("Insert cancelled");
		}
	}
	
	// SELECT
	public void select(ArrayList<ContactDTO> contacts) {
		if (contacts.isEmpty()) {
			System.out.println("No contact with that name");
			
		} else {
			contacts.forEach(System.out::println);
		}
	}
	
	// SELECT by name
	// SELECT 이름으로
	public void select(ContactDTO contact) {
		if (contact != null) {
			System.out.println(contact);
		} else {
			System.out.println("Select cancelled");
		}
	}
	
	// UPDATE
	public void update(ContactDTO contact) {
		if (contact != null) {
			System.out.println("Contact updated");
			System.out.println(contact);
		} else {
			System.out.println("Update cancelled");
		}
	}
	
	// DELETE
	public void delete(ContactDTO contact) {
		if (contact != null) {
			System.out.println("Contact deleted");
			System.out.println(contact);
		} else {
			System.out.println("Delete cancelled");
		}
	}
}
