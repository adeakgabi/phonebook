package com.b2international.phonebook3.rcp.contact;

import java.util.List;
import java.util.UUID;

import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Title;
import com.b2international.phonebook3.rcp.redux.Action;

public final class CreateContactAction implements Action {

	private final String id;
	private final Title title;
	private final String firstName;
	private final String lastName;
	private final String dateOfBirth;
	private final List<String> phoneNumber;
	private final List<Address> addresses;

	public CreateContactAction(Title title, String firstName, String lastName, String dateOfBirth, List<String> phoneNumber, List<Address> addresses) {
		this(UUID.randomUUID().toString(), title, firstName, lastName, dateOfBirth, phoneNumber, addresses);
	}
	
	public CreateContactAction(
			String id,
			Title title, 
			String firstName, 
			String lastName, 
			String dateOfBirth, 
			List<String> phoneNumber,
			List<Address> addresses) {
		this.id = id;
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.addresses = addresses;
	}

	public String getId() {
		return id;
	}
	
	public Title getTitle() {
		return title;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public List<String> getPhoneNumber() {
		return phoneNumber;
	}

	public List<Address> getAddresses() {
		return addresses;
	}
	
}
