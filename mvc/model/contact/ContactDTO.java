package mvc.model.contact;

import mvc.model.address.AddressDTO;
import mvc.model.job.JobDTO;
import utils.exceptionhandler.ExceptionHandler;
import utils.exceptionhandler.Input;

/**
 * @packageName : mvc.model.contact
 * @fileNmae	: ContactDTO.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Contacts Data Transfer Object
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
	private String number;
	private String name;
	private AddressDTO address = null;
	private String relationship;
	private String nationality;
	private JobDTO job = null;
	public boolean backOut = false;
	
	public ContactDTO(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
	public ContactDTO(int id, 
				      String name, 
				      String number, 
				      AddressDTO address,
				      String relationship,
				      String nationality,
				      JobDTO job) {
		this.id = id;
		this.name = name;
		this.number = number;
		this.address = address;
		this.relationship = relationship;
		this.nationality = nationality;
		this.job = job;
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
	
	private void setAddress() {
		AddressDTO address = new AddressDTO(exceptionHandler);
		this.backOut = !address.didSetValues();
		this.address = address;
	}

	private void setRelationship() {
		System.out.print("Relationship: ");
		this.relationship = this.exceptionHandler.handleInputException(Input.RELATIONSHIP);
		if (this.relationship.equals("back")) {
			this.backOut = true;
		}
	}
	
	private void setNationality() {
		System.out.print("Nationality: ");
		this.nationality = this.exceptionHandler.handleInputException(Input.ADDRESS);
		if (this.nationality.equals("back")) {
			this.backOut = true;
		}
	}
	
	private void setJob() {
		String answer;
		JobDTO job;
		
		System.out.print("Would you like to enter " + this.name + "'s job? (y/n): ");
		answer = this.exceptionHandler.handleInputException(Input.YESORNO).toLowerCase();
		if (answer.equals("back")) {
			this.job = null;
		} else if (answer.charAt(0) == 'y') {
			job = new JobDTO(exceptionHandler);
			this.backOut = !job.didSetValues(this);
			if (job.getAddress() == null) {
				job.setAddress(this.address);
			}
			this.job = job; 
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
		this.setAddress();
		if (this.backOut) {
			return;
		}
		this.setRelationship();
		if (this.backOut) {
			return;
		}
		this.setNationality();
		if (this.backOut) {
			return;
		}
		this.setJob();
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

	public AddressDTO getAddress() {
		return this.address;
	}

	public String getRelationship() {
		return this.relationship;
	}
	
	public String getNationality() {
		return this.nationality;
	}
	
	public JobDTO getJob() {
		return this.job;
	}

	public boolean getBackOut() {
		return this.backOut;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	public void setJob(JobDTO job) {
		this.job = job;
	}
	
	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	@Override
	public String toString() {
		String home = this.address != null ? this.address.toString() : "Homeless";
		String job = this.job != null ? this.job.toString() : "Unemployeed";
		return "-----------------------------------------------------------------------------\n" 
			 + "name: " + this.name + "\n" 
			 + "number: " + this.number + "\n" 
			 + "address: " + home + "\n" 
			 + "relationship: " + this.relationship + "\n"
			 + "nationality: " + this.nationality + "\n"
			 + "job: \n"
			 + job.indent(4) + "\n"
			 + "-----------------------------------------------------------------------------";
	}
}
