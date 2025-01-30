package service;

import java.util.Scanner;

import mvc.model.contact.ContactDAO;
import mvc.model.contact.DataAccessObjectInterface;
import mvc.view.ContactView;

/**
 * @packageName : service
 * @fileNmae	: Service.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : DataAccessObjectInterface 메소드들을 호출하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class Service {
	private DataAccessObjectInterface contacts;
	private ContactView view = new ContactView();
	private Scanner scanner;
	
	public Service(Scanner scanner) {
		this.scanner = scanner;
		this.contacts = new ContactDAO(scanner);
	}
	
	public void insert() {
		System.out.println("----------------------------------");
		System.out.println("          Create contact");
		System.out.println("----------------------------------");
		this.view.insert(this.contacts.insert());
	}
	
	public void select() {
		System.out.println("--------------------------------");
		System.out.println("            Contacts");
		System.out.println("--------------------------------");
		this.view.select(this.contacts.select());
	}
	
	public void selectWithName() {
		System.out.println("--------------------------------");
		System.out.println("            Contacts");
		System.out.println("--------------------------------");
		System.out.print("name of contact: ");
		this.view.select(this.contacts.select(scanner.nextLine()));
	}
	
	public void update() {
		System.out.println("--------------------------------");
		System.out.println("             Update");
		System.out.println("--------------------------------");
		System.out.print("Name of the contact to update: ");
		this.view.update(this.contacts.update());
	}
	
	public void delete() {
		System.out.println("--------------------------------");
		System.out.println("             Delete");
		System.out.println("--------------------------------");
		System.out.print("Name of the contact to delete: ");
		this.view.delete(this.contacts.delete());
	}
	
	public void close() {
		System.out.println("Closing");
		this.scanner.close();
	}
}
