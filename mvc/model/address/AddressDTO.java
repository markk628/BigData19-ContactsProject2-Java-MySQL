package mvc.model.address;

import utils.exceptionhandler.ExceptionHandler;
import utils.exceptionhandler.Input;

/**
 * @packageName : mvc.model.address
 * @fileNmae	: AddressVO.java
 * @author		: mark
 * @date		: 2025.02.04
 * @description : address Data Transfer Object 
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.02.04		MARK KIM		FIRST CREATED
 */
public class AddressDTO {
	private ExceptionHandler exceptionHandler;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	public boolean backOut = false;
	
	public AddressDTO(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
	public AddressDTO(String streetAddress, String city, String state, String zipCode, String country) {
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
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
	
	public boolean didSetValues() {
		this.setStreetAddress();
		if (this.backOut) {
			return false;
		}
		this.setCity();
		if (this.backOut) {
			return false;
		}
		this.setState();
		if (this.backOut) {
			return false;
		}
		this.setZipCode();
		if (this.backOut) {
			return false;
		}
		this.setCountry();
		if (this.backOut) {
			return false;
		}
		return true;
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

	@Override
	public String toString() {
		return streetAddress + ", " + city + ", " + state + " " + zipCode + ", " + country;
	}
}
