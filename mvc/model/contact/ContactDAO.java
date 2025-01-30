package mvc.model.contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import mvc.model.address.AddressDAO;
import mvc.model.relationship.RelationshipDAO;
import utils.DBConnection;
import utils.exceptionhandler.ExceptionHandler;
import utils.exceptionhandler.Input;
import utils.query.QueryBuilder;
import utils.query.SelectOption;
import utils.tables.AddressFields;
import utils.tables.CityFields;
import utils.tables.ContactFields;
import utils.tables.CountryFields;
import utils.tables.RelationshipFields;
import utils.tables.StateFields;

/**
 * @packageName : mvc.model.contact
 * @fileNmae	: ContactDAO.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Contact table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class ContactDAO implements DataAccessObjectInterface {
	private ExceptionHandler exceptionHandler;
	private AddressDAO addressDAO = new AddressDAO();
	private RelationshipDAO relationshipDAO = new RelationshipDAO();
	private DBConnection mainConnection = new DBConnection();

	public ContactDAO(Scanner scanner) {
		this.exceptionHandler = new ExceptionHandler(scanner);
	}

	// 입력받은 number를 가지고있는 contact가 있으면 그 contact를 반환하는 메소드 (없으면 null 반환)
	private ContactDTO getContactIfContactInContactTable(String number) {
		ContactDTO contact = null;
		DBConnection dbConnection = new DBConnection();
		ResultSet rs = null;
		dbConnection.connect(QueryBuilder.select(SelectOption.NUMBER));
		try {
			dbConnection.getPreparedStatement().setString(1, number);
			rs = dbConnection.executeQuery();
			if (rs.next()) {
				contact = new ContactDTO(rs.getInt(ContactFields.ID.toString()),
										 rs.getString(ContactFields.NAME.toString()),
										 rs.getString(ContactFields.NUMBER.toString()), 
										 rs.getString(AddressFields.ADDRESS.toString()),
										 rs.getString(CityFields.CITY.toString()),
										 rs.getString(StateFields.STATE.toString()),
										 rs.getString(AddressFields.ZIPCODE.toString()),
										 rs.getString(CountryFields.COUNTRY.toString()),
										 rs.getString(RelationshipFields.RELATIONSHIP.toString()));
			}
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:getContactIfContactInContactTable");
		} finally {
			dbConnection.disconnect();
		}
		return contact;
	}

	// contact를 추가하는 메소드
	@Override
	public ContactDTO insert() {
		ContactDTO contact;
		int addressID;
		int relationshipID;

		contact = new ContactDTO(this.exceptionHandler);
		contact.setValues(number -> { // 입력받은 번호를 가지고있는 contact가 있으면 그 contact를 수정하고 싶냐고 물어본다
			ContactDTO existingContact = this.getContactIfContactInContactTable(number);
			if (existingContact != null) {
				System.out.println("Contact with number already exists.");
				System.out.println(existingContact);
				System.out.println("Would you like to update the contact? (y/n): ");
				contact.cancel();
				if (this.exceptionHandler.handleInputException(Input.YESORNO).equals("y")) {
					this.update(existingContact);
				}
			}
		});
		if (contact.getBackOut()) {
			return null;
		}
		addressID = this.addressDAO.insertAddress(contact.getAddress(), 
												  contact.getCity(), 
												  contact.getState(), 
												  contact.getCountry(), 
												  contact.getZipCode());
		if (addressID == 0) {
			System.out.println("Error inserting address");
			return null;
		}
		relationshipID = this.relationshipDAO.insertRelationship(contact.getRelationship());
		if (relationshipID == 0) {
			System.out.println("Error inserting relationship");
			return null;
		}
		this.mainConnection.connect(QueryBuilder.insert());
		try {
			this.mainConnection.getPreparedStatement().setString(1, contact.getName());
			this.mainConnection.getPreparedStatement().setString(2, contact.getNumber());
			this.mainConnection.getPreparedStatement().setInt(3, addressID);
			this.mainConnection.getPreparedStatement().setInt(4, relationshipID);
			this.mainConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:insert");
		} finally {
			this.mainConnection.disconnect();
		}
		return contact;
	}

	// contact들을 반환하는 메소드
	@Override
	public ArrayList<ContactDTO> select() {
		ArrayList<ContactDTO> contactList = new ArrayList<>();
		ResultSet rs = null;	

		this.mainConnection.connect(QueryBuilder.select(SelectOption.ALL));
		try {
			rs = this.mainConnection.executeQuery();
			while (rs.next()) {
				contactList.add(new ContactDTO(rs.getInt(ContactFields.ID.toString()),
											   rs.getString(ContactFields.NAME.toString()),
											   rs.getString(ContactFields.NUMBER.toString()), 
											   rs.getString(AddressFields.ADDRESS.toString()),
											   rs.getString(CityFields.CITY.toString()),
											   rs.getString(StateFields.STATE.toString()),
											   rs.getString(AddressFields.ZIPCODE.toString()),
											   rs.getString(CountryFields.COUNTRY.toString()),
											   rs.getString(RelationshipFields.RELATIONSHIP.toString())));
			}
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:select");
			return null;
		} finally {
			this.mainConnection.disconnect();
		}
		return contactList;
	}

	// 받은 name을 가지고 있는 contact들을 반환하는 메소드
	@Override
	public ArrayList<ContactDTO> select(String name) {
		ArrayList<ContactDTO> contactList = new ArrayList<>();
		ResultSet rs = null;

		this.mainConnection.connect(QueryBuilder.select(SelectOption.NAME));
		try {
			this.mainConnection.getPreparedStatement().setString(1, name);
			rs = this.mainConnection.executeQuery();
			while (rs.next()) {
				contactList.add(new ContactDTO(rs.getInt(ContactFields.ID.toString()),
											   rs.getString(ContactFields.NAME.toString()),
											   rs.getString(ContactFields.NUMBER.toString()), 
											   rs.getString(AddressFields.ADDRESS.toString()),
											   rs.getString(CityFields.CITY.toString()),
											   rs.getString(StateFields.STATE.toString()),
											   rs.getString(AddressFields.ZIPCODE.toString()),
											   rs.getString(CountryFields.COUNTRY.toString()),
											   rs.getString(RelationshipFields.RELATIONSHIP.toString())));
			}
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:select(String name)");
			return null;
		} finally {
			this.mainConnection.disconnect();
		}
		return contactList;
	}

	// 받은 contactsList에서 원하는 contact를 반환하는 메소드
	private ContactDTO getContact(ArrayList<ContactDTO> contactsList) {
		int nameCount = contactsList.size();
		int selection;
		String option;

		if (nameCount == 1) {
			return contactsList.get(0);
		} else if (contactsList.isEmpty()) {
			System.out.println("You have no contacts");
			return null;
		}
		System.out.println("There are " + nameCount + " people with the name " + contactsList.get(0).getName());
		System.out.println("Select the contact to update");
		for (int i = 0; i < nameCount; i++) {
			System.out.println((i + 1) + ". " + "\n" + contactsList.get(i));
		}
		while (true) {
			System.out.print("Contact: ");
			option = this.exceptionHandler.handleInputException(Input.OPTION);
			if (option.equals("back")) {
				return null;
			}
			selection = this.exceptionHandler.handleParseIntException(option);
			if ((selection > 0) && (selection <= nameCount)) {
				return contactsList.get(selection - 1);
			} else {
				System.out.println(selection + " is not an option");
			}
		}
	}

	// 자식이 없는 부모row를 삭제하는 메소드
	private void deleteParentWithNoChild(ArrayList<String> queries) {
		this.mainConnection.connectAndExecuteBatches(queries);
	}

	// 받은 chosenFields기반으로 ContactFields enum목롤을 반환하는 메소드
	private ArrayList<ContactFields> getContactFields(String[] chosenFields) {
		ArrayList<String> fields = new ArrayList<>();
		for (String field: chosenFields) {
			fields.add(field);
		}
		Collections.sort(fields);
		ArrayList<ContactFields> contactFields = new ArrayList<>();
		for (String field: chosenFields) {
			switch (field) {
			case "1":
				contactFields.add(ContactFields.NAME);
				break;
			case "2":
				contactFields.add(ContactFields.NUMBER);
				break;
			case "3":
				contactFields.add(ContactFields.ADDRESS);
				break;
			case "4":
				contactFields.add(ContactFields.RELATIONSHIP);
				break;
			default:
				System.out.println(field + " is not an option");
			}
		}
		return contactFields;
	}

	// 받은 contact의 address column들을 수정하고 ID를 반환하는 메소드
	// updatedContact는 update() 메소드의 반환값이고, 수정한 정보를 저장하기위해 reference로 받음
	private int updateAddressFields(ContactDTO contact, String[] chosenFields, ContactDTO updatedContact, ArrayList<String> queries) {
		String streetAddress = contact.getStreetAddress();
		String city = contact.getCity();
		String state = contact.getState();
		String zipCode = contact.getZipCode();
		String country = contact.getCountry();

		for (String field: chosenFields) {
			switch (field) {
			case "1":
				System.out.print("New street address: ");
				streetAddress = this.exceptionHandler.handleInputException(Input.ADDRESS);
				queries.add(QueryBuilder.deleteAddressWithNoChild());
				updatedContact.setStreetAddress(streetAddress);
				break;
			case "2":
				System.out.print("New city: ");
				city = this.exceptionHandler.handleInputException(Input.ADDRESS);
				queries.add(QueryBuilder.deleteCityWithNoChild());
				updatedContact.setCity(city);
				break;
			case "3":
				System.out.print("New state: ");
				state = this.exceptionHandler.handleInputException(Input.ADDRESS);
				queries.add(QueryBuilder.deleteStateWithNoChild());
				updatedContact.setState(state);
				break;
			case "4":
				System.out.print("New country: ");
				country = this.exceptionHandler.handleInputException(Input.ADDRESS);
				queries.add(QueryBuilder.deleteCountryWithNoChild());
				updatedContact.setCountry(country);
				break;
			case "5":
				System.out.print("New zip code: ");
				zipCode = this.exceptionHandler.handleInputException(Input.ADDRESS);
				updatedContact.setZipCode(zipCode);
				break;
			default:
				System.out.println(field + " is not an option");
				break;
			}
		}
		return this.addressDAO.insertAddress(streetAddress, city, state, country, zipCode);
	}

	// 받은 contact를 수정하는 메소드
	private ContactDTO update(ContactDTO contact) {
		ArrayList<String> deleteQueries = new ArrayList<>();
		String chosenFields;
		ArrayList<ContactFields> fields = new ArrayList<>();
		int fieldsLength;
		String name;
		String number;
		int addressID;
		int relationshipID;
		String relationship;
		ContactDTO updatedContact = new ContactDTO(contact.getID(), 
												   contact.getName(), 
												   contact.getNumber(), 
												   contact.getStreetAddress(), 
												   contact.getCity(), 
												   contact.getState(), 
												   contact.getZipCode(), 
												   contact.getCountry(), 
												   contact.getRelationship());
		System.out.println("Enter fields(s) to update (ex. 1, 3, 4)");
		System.out.println("1. Name");
		System.out.println("2. Number");
		System.out.println("3. Address");
		System.out.println("4. Relationship");
		System.out.print("Field(s): ");
		chosenFields = this.exceptionHandler.handleInputException(Input.OPTION);
		if (chosenFields.toLowerCase().contains("back")) {
			return null;
		}
		fields = this.getContactFields(chosenFields.replace(" ", "").split(","));
		this.mainConnection.connect(QueryBuilder.update(fields));
		try {
			fieldsLength = fields.size();
			for (int i = 0; i < fieldsLength; i++) {
				switch (fields.get(i)) {
				case ID:
					break;
				case NAME:
					System.out.print("New name: ");
					name = this.exceptionHandler.handleInputException(Input.NAME);
					if (name.equals("back")) {
						this.mainConnection.disconnect();
						return null;
					}
					this.mainConnection.getPreparedStatement().setString(i + 1, name);
					updatedContact.setName(name);
					break;
				case NUMBER:
					System.out.print("New number: ");
					number = this.exceptionHandler.handleInputException(Input.NUMBER);
					if (number.equals("back")) {
						this.mainConnection.disconnect();
						return null;
					} else if (this.getContactIfContactInContactTable(number) != null) {
						System.out.println("number already exists");
						i--;
						break;
					}
					this.mainConnection.getPreparedStatement().setString(i + 1, number);
					updatedContact.setNumber(number);
					break;
				case ADDRESS:
					System.out.println("Enter fields(s) to update (ex. 1, 3, 4)");
					System.out.println("1. Street Address");
					System.out.println("2. City");
					System.out.println("3. State");
					System.out.println("4. Country");
					System.out.println("5. Zip Code");
					System.out.print("Field(s): ");
					chosenFields = this.exceptionHandler.handleInputException(Input.OPTION);
					if (chosenFields.toLowerCase().contains("back")) {
						this.mainConnection.disconnect();
						return null;
					}
					addressID = this.updateAddressFields(contact, chosenFields.replace(" ", "").split(","), updatedContact, deleteQueries);
					if (addressID == 0) {
						this.mainConnection.disconnect();
						return null;
					}
					this.mainConnection.getPreparedStatement().setInt(i + 1, addressID);
					break;
				case RELATIONSHIP:
					System.out.print("New relationship: ");
					relationship = this.exceptionHandler.handleInputException(Input.RELATIONSHIP);
					relationshipID = this.relationshipDAO.insertRelationship(relationship);
					if (relationshipID == 0) {
						System.out.println("Error inserting relationship");
						this.mainConnection.disconnect();
						return null;
					}
					this.mainConnection.getPreparedStatement().setInt(i + 1, relationshipID);
					deleteQueries.add(QueryBuilder.deleteRelationshipWithNoChild());
					updatedContact.setRelationship(relationship);
					break;
				}
			}
			this.mainConnection.getPreparedStatement().setInt(fieldsLength + 1, contact.getID());
			this.mainConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:update(ContactDTO contact)");
		} finally {
			this.mainConnection.disconnect();
		}
		this.deleteParentWithNoChild(deleteQueries);
		return updatedContact;
	}

	// contact를 수정하는 메소드
	@Override
	public ContactDTO update() {
		ContactDTO contactToUpdate;
		String name;

		name = this.exceptionHandler.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return null;
		}
		contactToUpdate = this.getContact(this.select(name));
		if (contactToUpdate == null) {
			return null;
		}
		return this.update(contactToUpdate);
	}

	// contact를 삭제하는 메소드
	@Override
	public ContactDTO delete() {
		ArrayList<String> deleteQueries = new ArrayList<>();
		ContactDTO contactToUpdate;
		String name;

		name = this.exceptionHandler.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return null;
		}
		contactToUpdate = this.getContact(this.select(name));
		if (contactToUpdate == null) {
			return null;
		}
		this.mainConnection.connect(QueryBuilder.delete());
		try {
			this.mainConnection.getPreparedStatement().setInt(1, contactToUpdate.getID());
			this.mainConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:delete");
		} finally {
			this.mainConnection.disconnect();
		}
		deleteQueries.add(QueryBuilder.deleteRelationshipWithNoChild());
		deleteQueries.add(QueryBuilder.deleteAddressWithNoChild());
		deleteQueries.add(QueryBuilder.deleteCityWithNoChild());
		deleteQueries.add(QueryBuilder.deleteStateWithNoChild());
		deleteQueries.add(QueryBuilder.deleteCountryWithNoChild());
		deleteParentWithNoChild(deleteQueries);
		return contactToUpdate;
	}
}
