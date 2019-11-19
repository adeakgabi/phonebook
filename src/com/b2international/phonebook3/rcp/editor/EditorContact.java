package com.b2international.phonebook3.rcp.editor;

import java.time.LocalDate;
import java.util.List;

import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.model.Title;

public class EditorContact {
	
	private String id;
	private Title title;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private List<String> phoneNumbers;
	private List<Address> addresses;
	
	public EditorContact(Contact contact) {
		this.id = contact.getId();
		this.title = contact.getTitle();
		this.firstName = contact.getFirstName();
		this.lastName = contact.getLastName();
		this.dateOfBirth = contact.getDateOfBirth();
		this.phoneNumbers = contact.getPhoneNumbers();
		this.addresses = contact.getAddresses();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
}
