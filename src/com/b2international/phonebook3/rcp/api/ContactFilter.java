package com.b2international.phonebook3.rcp.api;

import java.util.List;

import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;
import com.google.common.base.Strings;

public final class ContactFilter {
	
	public static final class ContactFilterBuilder {
		
		private String title;
		private String firstName;
		private String lastName;
		private String startDate;
		private String endDate;
		private String phoneNumber;
		private String address;
		
		public ContactFilterBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		public ContactFilterBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public ContactFilterBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public ContactFilterBuilder startDate(String startDate) {
			this.startDate = startDate;
			return this;
		}
		
		public ContactFilterBuilder endDate(String endDate) {
			this.endDate = endDate;
			return this;
		}
		
		public ContactFilterBuilder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		
			return this;
		}
		
		public ContactFilterBuilder addresses(String address) {
			this.address = address;
			return this;
		}
		
		public ContactFilter build() {
			return new ContactFilter(title, firstName, lastName, startDate, endDate, phoneNumber, address);
		}
	
	}
	
	public static ContactFilterBuilder builder() {
		return new ContactFilterBuilder();
	}
	
	private final String title;
	private final String firstName;
	private final String lastName;
	private final String startDate;
	private final String endDate;	
	private final String phoneNumber;
	private final String address;
	
	private ContactFilter(String title, String firstName, String lastName, String startDate, String endDate, String phoneNumber,
			String address) {
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	public String getTitle() {
		return title;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStartDate() {
		return startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public boolean byTitle(Contact contact) {
		return !Strings.isNullOrEmpty(title) ? contact.getTitle().getDisplayTitle().equalsIgnoreCase(title) : true;
	}
	
	public boolean byFirstName(Contact contact) {
		return !Strings.isNullOrEmpty(firstName) ? contact.getFirstName().equalsIgnoreCase(firstName) : true;
	}
	
	public boolean byLastName(Contact contact) {
		return !Strings.isNullOrEmpty(lastName) ? contact.getLastName().equalsIgnoreCase(lastName) : true;
	}
	
	public boolean byDayOfBirth(Contact contact) {
		if(!Strings.isNullOrEmpty(startDate) && !Strings.isNullOrEmpty(endDate)) {
			return contact.checkIfDateIsWithinRange(startDate, endDate);
		}
		return true;
	}
	
	public boolean byPhoneNumber(Contact contact) {
		return !Strings.isNullOrEmpty(phoneNumber) ? contact.getPhoneNumbers().contains(phoneNumber) : true;
	}
	
	public boolean byAddress(Contact contact) {
		final List<Address> addresses = contact.getAddresses();
		if (!Strings.isNullOrEmpty(address)) {
			return addresses.stream().filter(a -> a.hasMatch(address)).findAny().isPresent();
		}
		return true;
	}
	
}	
