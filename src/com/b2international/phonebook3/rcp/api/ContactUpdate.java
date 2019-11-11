package com.b2international.phonebook3.rcp.api;

import java.time.LocalDate;
import java.util.List;

import com.b2international.phonebook3.rcp.exceptions.WrongInputException;
import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.model.Title;
import com.google.common.base.Strings;

public final class ContactUpdate {
	
	public static final class ContactUpdateBuilder {
		
		private String id;
		private Title title;
		private String firstName;
		private String lastName;
		private LocalDate dateOfBirth;
		private List<String> phoneNumbers;
		private List<Address> addresses;
		
		public ContactUpdateBuilder(String id) {
			this.id = id;
		}
		
		public ContactUpdateBuilder title(Title title) {
			this.title = title;
			return this;
		}
		
		public ContactUpdateBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public ContactUpdateBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public ContactUpdateBuilder dateOfBirth(LocalDate dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
			return this;
		}
		
		public ContactUpdateBuilder phoneNumbers(List<String> phoneNumbers) throws WrongInputException {
			if(phoneNumbers.isEmpty()) {
				throw new WrongInputException("At least one phone number is required!");
			}
			this.phoneNumbers = phoneNumbers;
			return this;
		}
		
		public ContactUpdateBuilder addresses(List<Address> addresses) throws WrongInputException {
			if(addresses.isEmpty()) {
				throw new IllegalArgumentException("At least one address is required!");
			}
			this.addresses = addresses;
			return this;
		}
		
		public ContactUpdate build() {
			return new ContactUpdate(id, title, firstName, lastName, dateOfBirth, phoneNumbers, addresses);
		}
	}
	
	public static ContactUpdateBuilder builder(String id) {
		return new ContactUpdateBuilder(id);
	}
	
	private final String id;
	private final Title title;
	private final String firstName;
	private final String lastName;
	private final LocalDate dateOfBirth;
	private final List<String> phoneNumbers;
	private final List<Address> addresses;
	
	private ContactUpdate(
				final String id, final Title title, final String firstName,
				final String lastName, final LocalDate dateOfBirth,
				final List<String> phoneNumbers, final List<Address> addresses) {
		this.id = id;
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumbers = phoneNumbers;
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public boolean update(Contact contact) {
		boolean changed = false;
		if (getTitle() != null) {
			contact.setTitle(getTitle());
			changed = true;
		}
		
		if (!Strings.isNullOrEmpty(getFirstName())) {
			contact.setFirstName(getFirstName());
			changed = true;
		}
		
		if (!Strings.isNullOrEmpty(getLastName())) {
			contact.setLastName(getLastName());
			changed = true;
		}
		
		if (getDateOfBirth() != null) {
			contact.setDateOfBirth(getDateOfBirth());
			changed = true;
		}
		
		if (getPhoneNumbers() != null) {
			contact.setPhoneNumbers(getPhoneNumbers());
			changed = true;
		}
		
		if (getAddresses() != null) {
			contact.setAddresses(getAddresses());
			changed = true;
		}
		
		return changed;
	}

}
