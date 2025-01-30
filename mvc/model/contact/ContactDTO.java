package mvc.model.contact;

import utils.exceptionhandler.ExceptionHandler;
import utils.exceptionhandler.Input;

/**
 * @packageName : mvc.model.contact
 * @fileNmae	: ContactDTO.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Contacts DTO
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class ContactDTO {
	interface CheckIfNumberInTableHandler {
		void checkNumber(String number);
	}
	
	private ExceptionHandler exceptionHandler;
	private int id = 0;
	private String number = "back";
	private String name = "back";
	private String streetAddress = "back";
	private String city = "back";
	private String state = "back";
	private String zipCode = "back";
	private String country = "back";
	private String address = "back";
	private String relationship = "back";
	public boolean backOut = false;
	
	public ContactDTO(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
	public ContactDTO(int id, 
				      String name, 
				      String number, 
				      String streetAddress, 
				      String city, 
				      String state, 
				      String zipCode,
				      String country,
				      String relationship) {
		this.id = id;
		this.name = name;
		this.number = number;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
		this.address = streetAddress + ", " + city + ", " + state + " " + zipCode + ", " + country;
		this.relationship = relationship;
	}
	
	private void setNumber() {
		System.out.print("Number (ex. 01011112222): ");
		this.number = this.exceptionHandler.handleInputException(Input.NUMBER);
		if (this.number.equals("back")) {
			this.backOut = true;
		}
	}

	private void setName() {
		System.out.print("Name: ");
		this.name = this.exceptionHandler.handleInputException(Input.NAME);
		if (this.name.equals("back")) {
			this.backOut = true;
		}
	}

	private void setStreetAddress() {
		System.out.print("Street address: ");
		this.streetAddress = this.exceptionHandler.handleInputException(Input.ADDRESS);
		if (this.streetAddress.equals("back")) {
			this.backOut = true;
		}
	}

	private void setCity() {
		System.out.print("City: ");
		this.city = this.exceptionHandler.handleInputException(Input.ADDRESS);
		if (this.city.equals("back")) {
			this.backOut = true;
		}
	}

	private void setState() {
		System.out.print("State: ");
		this.state = this.exceptionHandler.handleInputException(Input.ADDRESS);
		if (this.state.equals("back")) {
			this.backOut = true;
		}
	}

	private void setZipCode() {
		System.out.print("Zip code: ");
		this.zipCode = this.exceptionHandler.handleInputException(Input.ADDRESS);
		if (this.zipCode.equals("back")) {
			this.backOut = true;
		}
	}

	private void setCountry() {
		System.out.print("Country: ");
		this.country = this.exceptionHandler.handleInputException(Input.ADDRESS);
		if (this.country.equals("back")) {
			this.backOut = true;
		}
	}

	private void setRelationship() {
		System.out.print("Relationship: ");
		this.relationship = this.exceptionHandler.handleInputException(Input.RELATIONSHIP);
		if (this.relationship.equals("back")) {
			this.backOut = true;
		}
	}
	
	public void setValues(CheckIfNumberInTableHandler checkNumberHandler) {
		this.setNumber();
		if (this.backOut) {
			return;
		}
		checkNumberHandler.checkNumber(this.number);
		if (this.backOut) {
			return;
		}
		this.setName();
		if (this.backOut) {
			return;
		}
		this.setStreetAddress();
		if (this.backOut) {
			return;
		}
		this.setCity();
		if (this.backOut) {
			return;
		}
		this.setState();
		if (this.backOut) {
			return;
		}
		this.setCountry();
		if (this.backOut) {
			return;
		}
		this.setZipCode();
		if (this.backOut) {
			return;
		}
		this.address = this.streetAddress + ", " + this.city + ", " + this.state + " " + this.zipCode + ", " + this.country;
		this.setRelationship();
	}
	
	public void cancel() {
		this.backOut = true;
	}
	
	public int getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getNumber() {
		return this.number;
	}
	
	public String getStreetAddress() {
		return this.streetAddress;
	}

	public String getCity() {
		return this.city;
	}

	public String getState() {
		return this.state;
	}
	
	public String getZipCode() {
		return this.zipCode;
	}

	public String getCountry() {
		return this.country;
	}

	public String getAddress() {
		return this.address;
	}

	public String getRelationship() {
		return this.relationship;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public boolean getBackOut() {
		return this.backOut;
	}

	@Override
	public String toString() {
		return "-----------------------------------------------------------------------------\n" 
			 + "name: " + this.name + "\n" 
			 + "number: " + this.number + "\n" 
			 + "address: " + this.address + "\n" 
			 + "relationship: " + this.relationship + "\n"
			 + "-----------------------------------------------------------------------------";
	}
}
