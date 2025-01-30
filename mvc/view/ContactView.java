package mvc.view;

import java.util.ArrayList;

import mvc.model.contact.ContactDTO;

/**
 * @packageName : mvc.view
 * @fileNmae	: ContactView.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Contacts database제어 결과를 출력하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class ContactView {
	
	public void insert(ContactDTO contact) {
		if (contact != null) {
			System.out.println("Contact added");
			System.out.println(contact);
		} else {
			System.out.println("Insert cancelled");
		}
	}
	
	public void select(ArrayList<ContactDTO> contacts) {
		if (contacts.isEmpty()) {
			System.out.println("Contacts empty");
		} else {
			contacts.forEach(System.out::println);
		}
	}
	
	public void select(ContactDTO contact) {
		if (contact != null) {
			System.out.println(contact);
		} else {
			System.out.println("Select cancelled");
		}
	}
	
	public void update(ContactDTO contact) {
		if (contact != null) {
			System.out.println("Contact updated");
			System.out.println(contact);
		} else {
			System.out.println("Update cancelled");
		}
	}
	
	public void delete(ContactDTO contact) {
		if (contact != null) {
			System.out.println("Contact deleted");
			System.out.println(contact);
		} else {
			System.out.println("Delete cancelled");
		}
	}
}
