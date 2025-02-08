package mvc.model.job;

import mvc.model.address.AddressDTO;
import mvc.model.contact.ContactDTO;
import utils.exceptionhandler.ExceptionHandler;
import utils.exceptionhandler.Input;

/**
 * @packageName : mvc.model.job
 * @fileNmae	: JobVO.java
 * @author		: mark
 * @date		: 2025.02.04
 * @description : Job Data Transfer Object
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.02.04		MARK KIM		FIRST CREATED
 */
public class JobDTO {
	private ExceptionHandler exceptionHandler;
	private String profession;
	private String company;
	private AddressDTO address = null;
	public boolean backOut = false;
	
	public JobDTO() {
		this.profession = "";
		this.company = "";
		this.address = null;
	}
	
	public JobDTO(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
	public JobDTO(String profession, String company, AddressDTO address) {
		this.profession = profession;
		this.company = company;
		this.address = address;
	}
	
	private void setProfession() {
		System.out.print("Enter profession: ");
		this.profession = this.exceptionHandler.handleInputException(Input.NAME);
		if (this.profession.equals("back")) {
			this.backOut = true;
		}
	}
	
	private void setCompany() {
		System.out.print("Enter company: ");
		this.company = this.exceptionHandler.handleInputException(Input.NAME);
		if (this.company.equals("back")) {
			this.backOut = true;
		}
	}
	
	private void setAddress() {
		AddressDTO address = new AddressDTO(exceptionHandler);
		this.backOut = !address.didSetValues();
		this.address = address;
	}
	
	public boolean didSetValues(ContactDTO contact) {
		String answer;
		
		this.setProfession();
		if (this.backOut) {
			return false;
		}
		this.setCompany();
		if (this.backOut) {
			return false;
		}
		System.out.print("Does " + contact.getName() + " work from home? (y/n): ");
		answer = this.exceptionHandler.handleInputException(Input.YESORNO).toLowerCase();
		if (answer.equals("back")) {
			return false;
		} else if (answer.charAt(0) == 'y') {
			this.setAddress(contact.getAddress());
		} else if (answer.charAt(0) == 'n') {
			System.out.println("Enter " + contact.getName() + "'s work address");
			this.setAddress();
			if (this.backOut) {
				return false;
			}
		}
		return true;
	}

	public String getProfession() {
		return this.profession;
	}

	public String getCompany() {
		return this.company;
	}

	public AddressDTO getAddress() {
		return this.address;
	}
	
	public void setProfession(String profession) {
		this.profession = profession;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "profession: " + profession + "\n" 
			 + "company: " + company + "\n" 
			 + "address: " + address;
	}
}
