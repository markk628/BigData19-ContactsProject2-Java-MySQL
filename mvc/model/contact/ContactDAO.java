package mvc.model.contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import mvc.model.address.AddressDAO;
import mvc.model.address.AddressDTO;
import mvc.model.country.CountryDAO;
import mvc.model.job.JobDAO;
import mvc.model.job.JobDTO;
import mvc.model.relationship.RelationshipDAO;
import utils.DBConnection;
import utils.exceptionhandler.ExceptionHandler;
import utils.exceptionhandler.Input;
import utils.query.QueryBuilder;
import utils.query.SelectOption;
import utils.tables.AddressFields;
import utils.tables.CityFields;
import utils.tables.CompanyFields;
import utils.tables.ContactFields;
import utils.tables.CountryFields;
import utils.tables.ProfessionFields;
import utils.tables.RelationshipFields;
import utils.tables.StateFields;


/**
 * @packageName : mvc.model.contact
 * @fileNmae	: ContactDAO.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Controls the contact table 
 * 				  contact table을 제어하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.02.08		MARK KIM		FIRST CREATED
 */
public class ContactDAO implements DataAccessObjectInterface {
	private ExceptionHandler exceptionHandler;
	private AddressDAO addressDAO = new AddressDAO();
	private RelationshipDAO relationshipDAO = new RelationshipDAO();
	private CountryDAO countryDAO = new CountryDAO();
	private JobDAO jobDAO = new JobDAO();
	private DBConnection mainConnection = new DBConnection();

	public ContactDAO(Scanner scanner) {
		this.exceptionHandler = new ExceptionHandler(scanner);
	}

	// Creates a ContactDTO instance from data from the ResultSet passed in
	// Consequently should only be used when connecting to JDBC
	// 받은 ResultSet로 ContactDTO instance 만드는 메소드
	// JDBC와 연결할때만 사용
	private ContactDTO createContact(ResultSet rs) {
		ContactDTO contact = null;
		try {
			int id = rs.getInt(ContactFields.ID.toString());
			String name = rs.getString(ContactFields.NAME.toString());
			String number = rs.getString(ContactFields.NUMBER.toString());
			String streetAddress = rs.getString(AddressFields.ADDRESS.toString());
			String city = rs.getString(CityFields.CITY.toString());
			String state = rs.getString(StateFields.STATE.toString());
			String zipCode = rs.getString(AddressFields.ZIPCODE.toString());
			String country = rs.getString(CountryFields.COUNTRY.toString());
			AddressDTO address = streetAddress != null ? new AddressDTO(streetAddress, city, state, zipCode, country) : null;
			String relationship = rs.getString(RelationshipFields.RELATIONSHIP.toString());
			String nationality = rs.getString(ContactFields.nationality());
			String profession = rs.getString(ProfessionFields.PROFESSION.toString());
			String company = rs.getString(CompanyFields.COMPANY.toString());
			String jobStreetAddress = rs.getString(AddressFields.ADDRESS.toStringForJob());
			String jobCity = rs.getString(CityFields.CITY.toStringForJob());
			String jobState = rs.getString(StateFields.STATE.toStringForJob());
			String jobZipCode = rs.getString(AddressFields.ZIPCODE.toStringForJob());
			String jobCountry = rs.getString(CountryFields.COUNTRY.toStringForJob());
			AddressDTO jobAddress = new AddressDTO(jobStreetAddress, jobCity, jobState, jobZipCode, jobCountry);
			JobDTO job = profession != null ? new JobDTO(profession, company, jobAddress) : null;
			contact = new ContactDTO(id, name, number, address, relationship, nationality, job);
		} catch (SQLException e) {
			System.out.println("Error creating contact");
		}
		return contact;
	}

	// Returns the contact with the passed in phone number (if there are none will return null) 
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
				contact = this.createContact(rs);
			}
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:getContactIfContactInContactTable");
		} finally {
			dbConnection.disconnect();
		}
		return contact;
	}

	// Adds a contact to the database (INSERT)
	// contact를 추가하는 메소드
	@Override
	public ContactDTO insert() {
		ContactDTO contact;
		int addressID;
		int relationshipID;
		int nationalityID;
		int jobID = 0;
		boolean isEmployeed = false;

		contact = new ContactDTO(this.exceptionHandler);
		// if there is a contact with the inputed number, ask if user wants to update said number
		// 입력받은 number을 가지고있는 contact가 있으면 그 contact를 수정하고 싶냐고 물어본다
		contact.setValues(number -> { 
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
		isEmployeed = contact.getJob() != null;
		addressID = this.addressDAO.insertAndOrGetAddress(contact.getAddress());
		if (addressID == 0) {
			System.out.println("Error inserting address");
			return null;
		}
		relationshipID = this.relationshipDAO.insertRelationship(contact.getRelationship());
		if (relationshipID == 0) {
			System.out.println("Error inserting relationship");
			return null;
		}
		nationalityID = this.countryDAO.insertAndOrGetCountry(contact.getNationality());
		if (relationshipID == 0) {
			System.out.println("Error inserting nationality");
			return null;
		}
		if (isEmployeed) {
			jobID = this.jobDAO.insertAndOrGetJob(contact.getJob().getProfession(), contact.getJob().getCompany(),
					contact.getJob().getAddress());
			if (jobID == 0) {
				System.out.println("Error inserting job");
				return null;
			}
		}
		this.mainConnection.connect(QueryBuilder.insert(isEmployeed));
		try {
			this.mainConnection.getPreparedStatement().setString(1, contact.getName());
			this.mainConnection.getPreparedStatement().setString(2, contact.getNumber());
			this.mainConnection.getPreparedStatement().setInt(3, addressID);
			this.mainConnection.getPreparedStatement().setInt(4, relationshipID);
			this.mainConnection.getPreparedStatement().setInt(5, nationalityID);
			if (isEmployeed) {
				this.mainConnection.getPreparedStatement().setInt(6, jobID);
			}
			this.mainConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:insert");
		} finally {
			this.mainConnection.disconnect();
		}
		return contact;
	}

	// Returns a list of contacts in the database (SELECT)
	// contact들을 반환하는 메소드
	@Override
	public ArrayList<ContactDTO> select() {
		ArrayList<ContactDTO> contactList = new ArrayList<>();
		ResultSet rs = null;

		this.mainConnection.connect(QueryBuilder.select(SelectOption.ALL));
		try {
			rs = this.mainConnection.executeQuery();
			while (rs.next()) {
				contactList.add(this.createContact(rs));
			}
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:select");
			return null;
		} finally {
			this.mainConnection.disconnect();
		}
		return contactList;
	}

	// Returns the contact with the passed in name (SELECT)
	// 받은 name을 가지고 있는 contact들을 반환하는 메소드
	@Override
	public ArrayList<ContactDTO> select(String name) {
		ArrayList<ContactDTO> contactList = new ArrayList<>();
		ResultSet rs = null;

		this.mainConnection.connect(QueryBuilder.select(SelectOption.NAME));
		try {
			this.mainConnection.getPreparedStatement().setString(1, "%" + name + "%");
			rs = this.mainConnection.executeQuery();
			while (rs.next()) {
				contactList.add(this.createContact(rs));
			}
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:select(String name)");
			return null;
		} finally {
			this.mainConnection.disconnect();
		}
		return contactList;
	}

	// Returns the contact the user chooses from a list of contacts
	// needed when there are contacts with the same name
	// 받은 contactsList에서 원하는 contact를 반환하는 메소드
	// 같은 name의 contact가 있을 때 필요
	private ContactDTO getContact(ArrayList<ContactDTO> contactsList, String name) {
		int nameCount = contactsList.size();
		int selection;
		String option;

		if (nameCount == 1) {
			return contactsList.get(0);
		} else if (contactsList.isEmpty()) {
			System.out.println("You have no contacts");
			return null;
		}
		System.out.println("There are " + nameCount + " people with the name " + name);
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

	// Deletes rows from tables that have no child (DELETE)
	// 자식이 없는 부모row를 해당되는 table에서 삭제하는 메소드
	private void deleteParentWithNoChild(ArrayList<String> queries) {
		this.mainConnection.connectAndExecuteBatches(queries);
	}

	// Returns a list of ContactFields enums based on the passed in String array
	// 받은 chosenFields기반으로 ContactFields enum목롤을 반환하는 메소드
	private ArrayList<ContactFields> getContactFields(String[] chosenFields) {
		ArrayList<String> fields = new ArrayList<>();
		for (String field : chosenFields) {
			fields.add(field);
		}
		Collections.sort(fields);
		ArrayList<ContactFields> contactFields = new ArrayList<>();
		for (String field : chosenFields) {
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
			case "5":
				contactFields.add(ContactFields.JOB);
				break;
			default:
				System.out.println(field + " is not an option");
			}
		}
		return contactFields;
	}

	// Updates the passed in contact's name and sets the contact's name's value in the PreparedStatement
	// 받은 contact의 이름을 수정하고, PreparedStatement에서 contact의 name 값을 설정하는 메소드
	private boolean didUpdateName(ContactDTO contact, int i) {
		String name;

		System.out.print("New name: ");
		name = this.exceptionHandler.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return false;
		}
		this.mainConnection.setString(i, name);
		contact.setName(name);
		return true;
	}
	
	// Updates the passed in contact's address and returns a boolean indicating whether the user backed out or completed the update
	// Delete queries are added to queries based on what is updated in order to delete data that is no longer needed
	// 받은 contact의 address를 수정하고, 수정을 끝까지 했는지 여부하는 boolean을 반환하는 메소드
	// 필요없는 정보를 데이터배이스에서 지우기 위해 업데이트된 정보를 기반으로 queries에 삭제 쿼리를 추가
	private boolean didUpdateAddressFields(AddressDTO address, ArrayList<String> queries) {
		String streetAddress;
		String city;
		String state;
		String zipCode;
		String country;
		String chosenFields;
		List<String> chosenFieldsList;

		System.out.println("Enter fields(s) to update (ex. 1, 3, 4)");
		System.out.println("1. Street Address");
		System.out.println("2. City");
		System.out.println("3. State");
		System.out.println("4. Country");
		System.out.println("5. Zip Code");
		System.out.print("Field(s): ");
		chosenFields = this.exceptionHandler.handleInputException(Input.OPTION);
		if (chosenFields.toLowerCase().contains("back")) {
			return false;
		}
		chosenFieldsList = Arrays.asList(chosenFields.replace(" ", "").split(","));
		Collections.sort(chosenFieldsList);
		for (String field : chosenFieldsList) {
			switch (field) {
			case "1":
				System.out.print("New street address: ");
				streetAddress = this.exceptionHandler.handleInputException(Input.ADDRESS);
				if (streetAddress.equals("back")) {
					return false;
				}
				queries.add(QueryBuilder.deleteAddressWithNoChild());
				address.setStreetAddress(streetAddress);
				break;
			case "2":
				System.out.print("New city: ");
				city = this.exceptionHandler.handleInputException(Input.ADDRESS);
				if (city.equals("back")) {
					return false;
				}
				queries.add(QueryBuilder.deleteCityWithNoChild());
				address.setCity(city);
				break;
			case "3":
				System.out.print("New state: ");
				state = this.exceptionHandler.handleInputException(Input.ADDRESS);
				if (state.equals("back")) {
					return false;
				}
				queries.add(QueryBuilder.deleteStateWithNoChild());
				address.setState(state);
				break;
			case "4":
				System.out.print("New country: ");
				country = this.exceptionHandler.handleInputException(Input.ADDRESS);
				if (country.equals("back")) {
					return false;
				}
				queries.add(QueryBuilder.deleteCountryWithNoChild());
				address.setCountry(country);
				break;
			case "5":
				System.out.print("New zip code: ");
				zipCode = this.exceptionHandler.handleInputException(Input.ADDRESS);
				if (zipCode.equals("back")) {
					return false;
				}
				address.setZipCode(zipCode);
				break;
			default:
				System.out.println(field + " is not an option");
				break;
			}
		}
		return true;
	}

	// Updates the passed in contact's address and sets the contact's address' value in the PreparedStatement
	// 받은 contact의 주소를 수정하고, PreparedStatement의 contact의 name 값을 설정하는 메소드
	private boolean didUpdateAddress(ContactDTO contact, int i, ArrayList<String> queries, boolean forJob) {
		int addressID;
		String homeless;
		AddressDTO newAddress;
		
		while (true) {
			System.out.print("Is " + contact.getName() + " homeless?: (y/n) ");
			homeless = this.exceptionHandler.handleInputException(Input.YESORNO).toLowerCase();
			if (homeless.equals("back")) {
				return false;
			} else if (homeless.charAt(0) == 'y') {
				contact.setAddress(null);
				break;
			} else if (homeless.charAt(0) == 'n') {
				if (contact.getAddress() == null) {
					newAddress = new AddressDTO(exceptionHandler);
					if (!newAddress.didSetValues()) {
						return false;
					}
					contact.setAddress(newAddress);
				} else {
					if (!this.didUpdateAddressFields(contact.getAddress(), queries)) {
						return false;
					}
				}
				break;
			}
			System.out.println("not an option");
		}
		if (contact.getAddress() == null) {
			this.mainConnection.setNull(i);
		} else {
			addressID = this.addressDAO.insertAndOrGetAddress(contact.getAddress());
			if (addressID == 0) {
				return false;
			}
			this.mainConnection.setInt(i, addressID);
		}
		return true;
	}

	// Updates the passed in contact's relationship and sets the contact's relationship's value in the PreparedStatement
	// 받은 contact의 relationship을 수정하고, PreparedStatement의 contact의 relationship 값을 설정하는 메소드
	private boolean didUpdateRelationship(ContactDTO contact, int i, ArrayList<String> queries) {
		String relationship;
		int relationshipID;

		System.out.print("New relationship: ");
		relationship = this.exceptionHandler.handleInputException(Input.RELATIONSHIP);
		if (relationship.equals("back")) {
			return false;
		}
		relationshipID = this.relationshipDAO.insertRelationship(relationship);
		if (relationshipID == 0) {
			System.out.println("Error inserting relationship");
			return false;
		}
		queries.add(QueryBuilder.deleteRelationshipWithNoChild());
		this.mainConnection.setInt(i, relationshipID);
		contact.setRelationship(relationship);
		return true;
	}

	// Updates the passed in contact's job and returns a boolean indicating whether the user backed out or completed the update
	// Delete queries are added to queries based on what is updated in order to delete data that is no longer needed
	// 받은 contact의 job을 수정하고, 수정을 끝까지 했는지 여부하는 boolean을 반환하는 메소드
	// 필요없는 정보를 데이터배이스에서 지우기 위해 업데이트된 정보를 기반으로 queries에 삭제 쿼리를 추가
	private boolean didUpdateJobFields(ContactDTO contact, ArrayList<String> queries) {
		String profession;
		String company;
		String chosenFields;
		String workFromHome;
		List<String> chosenFieldsList;

		if (contact.getJob() == null) {
			contact.setJob(new JobDTO());
		}
		System.out.println("Enter fields(s) to update (ex. 1, 2, 3)");
		System.out.println("1. Profession/title");
		System.out.println("2. Company");
		System.out.println("3. Work address");
		chosenFields = this.exceptionHandler.handleInputException(Input.OPTION);
		if (chosenFields.toLowerCase().contains("back")) {
			return false;
		}
		chosenFieldsList = Arrays.asList(chosenFields.replace(" ", "").split(","));
		Collections.sort(chosenFieldsList);
		for (String field : chosenFieldsList) {
			switch (field) {
			case "1":
				System.out.print("New profession/title: ");
				profession = this.exceptionHandler.handleInputException(Input.NAME);
				if (profession.equals("back")) {
					return false;
				}
				contact.getJob().setProfession(profession);
				queries.add(0, QueryBuilder.deleteProfessionWithNoChild());
				break;
			case "2":
				System.out.print("New company: ");
				company = this.exceptionHandler.handleInputException(Input.NAME);
				if (company.equals("back")) {
					return false;
				}
				contact.getJob().setCompany(company);
				queries.add(0, QueryBuilder.deleteCompanyWithNoChild());
				break;
			case "3":
				while (true) {
					System.out.print("Does " + contact.getName() + " work from home? (y/n) ");
					workFromHome = this.exceptionHandler.handleInputException(Input.YESORNO).toLowerCase();
					if (workFromHome.equals("back")) { 
						return false;
					} else if (workFromHome.charAt(0) == 'y') {
						contact.getJob().setAddress(contact.getAddress());
						break;
					} else if (workFromHome.charAt(0) == 'n') {
						if (!this.didUpdateAddressFields(contact.getJob().getAddress(), queries)) {
							return false;
						}
						break;
					}	
					System.out.println("not an option");
				}
				break;
			default:
				System.out.println(field + " is not an option");
				break;
			}
		}
		queries.add(0, QueryBuilder.deleteJobWithNoChild());
		return true;
	}
	
	// Updates the passed in contact's job and sets the contact's job's value in the PreparedStatement
	// 받은 contact의 job을 수정하고, PreparedStatement의 contact의 name 값을 설정하는 메소드
	private boolean didUpdateJob(ContactDTO contact, int i, ArrayList<String> queries) {
		int jobID;
		String unemployeed;
		JobDTO newJob;

		while (true) {
			System.out.print("Is " + contact.getName() + " unemployeed?: (y/n) ");
			unemployeed = this.exceptionHandler.handleInputException(Input.YESORNO).toLowerCase(); 
			if (unemployeed.equals("back")) {
				return false;
			} else if (unemployeed.charAt(0) == 'y') {
				contact.setJob(null);
				queries.add(QueryBuilder.deleteJobWithNoChild());
				queries.add(QueryBuilder.deleteProfessionWithNoChild());
				queries.add(QueryBuilder.deleteCompanyWithNoChild());
				queries.add(QueryBuilder.deleteAddressWithNoChild());
				queries.add(QueryBuilder.deleteCityWithNoChild());
				queries.add(QueryBuilder.deleteStateWithNoChild());
				queries.add(QueryBuilder.deleteCountryWithNoChild());
				this.mainConnection.setNull(i);
				return true;
			} else if (unemployeed.charAt(0) == 'n') {
				if (contact.getJob() == null) {
					newJob = new JobDTO(exceptionHandler);
					if (!newJob.didSetValues(contact)) {
						return false;
					}
					contact.setJob(newJob);
				} else {
					if (!this.didUpdateJobFields(contact, queries)) {
						return false;
					}
				}
				jobID = this.jobDAO.insertAndOrGetJob(contact.getJob().getProfession(), 
													  contact.getJob().getCompany(),
													  contact.getJob().getAddress());
				if (jobID == 0) {
					return false;
				}
				this.mainConnection.setInt(i, jobID);
				break;
			}
			System.out.println("not an option");
		}
		return true;
	}

	// Updates the passed in contact
	// 받은 contact를 수정하는 메소드
	private ContactDTO update(ContactDTO contact) {
		ArrayList<String> deleteQueries = new ArrayList<>();
		String chosenFields;
		ArrayList<ContactFields> fields = new ArrayList<>();
		int fieldsLength;
		String number;
		System.out.println("Enter fields(s) to update (ex. 1, 3, 4)");
		System.out.println("1. Name");
		System.out.println("2. Number");
		System.out.println("3. Address");
		System.out.println("4. Relationship");
		System.out.println("5. Job");
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
					if (!this.didUpdateName(contact, i + 1)) {
						this.mainConnection.disconnect();
						return null;
					}
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
					this.mainConnection.setString(i + 1, number);
					contact.setNumber(number);
					break;
				case ADDRESS:
					if (!this.didUpdateAddress(contact, i + 1, deleteQueries, false)) {
						this.mainConnection.disconnect();
						return null;
					}
					break;
				case RELATIONSHIP:
					if (!this.didUpdateRelationship(contact, i + 1, deleteQueries)) {
						this.mainConnection.disconnect();
						return null;
					}
					break;
				case JOB:
					if (!this.didUpdateJob(contact, i + 1, deleteQueries)) {
						this.mainConnection.disconnect();
						return null;
					}
					break;
				default:
					break;
				}
			}
			this.mainConnection.getPreparedStatement().setInt(fieldsLength + 1, contact.getID());
			this.mainConnection.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException ContactDAO:update(ContactDTO contact)");
			return null;
		} finally {
			this.mainConnection.disconnect();
		}
		this.deleteParentWithNoChild(deleteQueries);
		return contact;
	}

	// Updates a contact (UPDATE)
	// contact를 수정하는 메소드
	@Override
	public ContactDTO update() {
		ContactDTO contactToUpdate;
		String name;

		name = this.exceptionHandler.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return null;
		}
		contactToUpdate = this.getContact(this.select(name), name);
		if (contactToUpdate == null) {
			return null;
		}
		return this.update(contactToUpdate);
	}

	// Deletes a contact (DELETE)
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
		contactToUpdate = this.getContact(this.select(name), name);
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
		deleteQueries.add(QueryBuilder.deleteJobWithNoChild());
		deleteQueries.add(QueryBuilder.deleteProfessionWithNoChild());
		deleteQueries.add(QueryBuilder.deleteCompanyWithNoChild());
		deleteQueries.add(QueryBuilder.deleteAddressWithNoChild());
		deleteQueries.add(QueryBuilder.deleteCityWithNoChild());
		deleteQueries.add(QueryBuilder.deleteStateWithNoChild());
		deleteQueries.add(QueryBuilder.deleteCountryWithNoChild());
		deleteParentWithNoChild(deleteQueries);
		return contactToUpdate;
	}
}
