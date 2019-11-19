package com.b2international.phonebook3.rcp.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

@JsonDeserialize(builder = Contact.ContactBuilder.class)
public class Contact {
	
	@JsonPOJOBuilder(withPrefix = "")
	public static class ContactBuilder {
		
		private String id = UUID.randomUUID().toString();
		private Title title = Title.DEFAULT;
		private String firstName;
		private String lastName;
		private LocalDate dateOfBirth = LocalDate.now();
		private List<String> phoneNumbers = Collections.emptyList();
		private List<Address> addresses = Collections.emptyList();
		
		ContactBuilder() {}
		
		public ContactBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public ContactBuilder title(Title title) {
			this.title = title;
			return this;
		}
		
		public ContactBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public ContactBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public ContactBuilder dateOfBirth(LocalDate dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
			return this;
		}
		
		@JsonProperty("phoneNumber")
		public ContactBuilder phoneNumbers(List<String> phoneNumbers) {
			this.phoneNumbers = phoneNumbers;
			return this;
		}
		
		@JsonProperty("address")
		public ContactBuilder addresses(List<Address> addresses) {
			this.addresses = addresses;
			return this;
		}
		
		public Contact build() {
			return new Contact(id, title, firstName, lastName, dateOfBirth, phoneNumbers, addresses);
		}
	}
	
	@JsonIgnore
	private final String id;
	@JsonInclude(Include.NON_EMPTY)
	private final Title title;
	private final String firstName;
	private final String lastName;
	private final LocalDate dateOfBirth;
	@JsonProperty("phoneNumber")
	private final List<String> phoneNumbers;
	@JsonProperty("address")
	private final List<Address> addresses;
	
	private Contact(
			String id, 
			Title title, 
			String firstName, 
			String lastName, 
			LocalDate dateOfBirth, 
			List<String> phoneNumbers, 
			List<Address> addresses) {
		this.id = Preconditions.checkNotNull(id);
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumbers = phoneNumbers == null ? Collections.emptyList() : Lists.newArrayList(phoneNumbers);
		this.addresses = addresses == null ? Collections.emptyList() : Lists.newArrayList(addresses);
	}
	
	public String getId() {
		return id;
	}

	public Title getTitle() {
		if(title == null) {
			return Title.DEFAULT;
		}
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

	public static Contact createEmptyContact() {
		Contact contact = builder()
				.firstName("")
				.lastName("")
				.build();
		
		return contact;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, firstName, lastName, phoneNumbers, addresses, dateOfBirth);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Contact other = (Contact) obj;
		return Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName)
				&& Objects.equals(dateOfBirth, other.dateOfBirth)
				&& Objects.equals(title, other.title)
				&& Objects.equals(phoneNumbers, other.phoneNumbers)
				&& Objects.equals(addresses, other.addresses)
				&& Objects.equals(id, other.id);
	}

	public static String toDateString(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	@JsonIgnore
	public boolean checkIfDateIsWithinRange(String startDate, String endDate) {
		LocalDate formattedStartDate = toDate(startDate);
		LocalDate formattedEndDate = toDate(endDate);
		return !(dateOfBirth.isBefore(formattedStartDate) || dateOfBirth.isAfter(formattedEndDate));
	}

	@Override
	public String toString() {
		return "Contact [title=" + title + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", dateOfBirth=" + dateOfBirth + ", phoneNumbers=" + phoneNumbers + ", addresses=" + addresses + "]";
	}

	public Contact withTitle(Title title) {
		if (title == this.title) return this;
		return builder(this)
				.title(title)
				.build();
	}

	public Contact withFirstName(String firstName) {
		if(this.firstName.equals(firstName)) return this;
		return builder(this)
				.firstName(firstName)
				.build();
	}

	public Contact withLastName(String lastName) {
		if(this.lastName.equals(lastName)) return this;
		return builder(this)
				.lastName(lastName)
				.build();
	}

	public Contact withDateOfBirth(LocalDate dateOfBirth) {
		if(this.dateOfBirth.equals(dateOfBirth)) return this;
		return builder(this)
				.dateOfBirth(dateOfBirth)
				.build();
	}

	public Contact withPhoneNumber(List<String> phoneNumbers) {
		if(this.phoneNumbers.containsAll(phoneNumbers)) return this;
		return builder(this)
				.phoneNumbers(phoneNumbers)
				.build();	
	}

	public Contact withAddress(List<Address> addresses) {
		if(this.addresses.containsAll(addresses)) return this;
		return builder(this)
				.addresses(addresses)
				.build();
	}

	public static Contact.ContactBuilder builder() {
		return new ContactBuilder();
	}
	
	public static Contact.ContactBuilder builder(Contact contact) {
		return new ContactBuilder()
				.id(contact.getId())
				.title(contact.getTitle())
				.firstName(contact.getFirstName())
				.lastName(contact.getLastName())
				.dateOfBirth(contact.getDateOfBirth())
				.phoneNumbers(contact.getPhoneNumbers())
				.addresses(contact.getAddresses());
	}
	
}
