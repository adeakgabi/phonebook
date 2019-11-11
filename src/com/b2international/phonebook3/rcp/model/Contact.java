package com.b2international.phonebook3.rcp.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class Contact {
	
	@JsonIgnore
	private String id;
	@JsonInclude(Include.NON_EMPTY)
	private Title title = Title.DEFAULT;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	@JsonProperty("phoneNumber")
	private List<String> phoneNumbers;
	private List<Address> addresses;
	
	public Contact(String firstName, String lastName, String dateOfBirth, List<String> phoneNumbers, List<Address> addresses) {
		this(Title.DEFAULT, firstName, lastName, dateOfBirth, phoneNumbers, addresses);
	}
	
	public Contact(Contact contact) {
		this.id = contact.getId();
		this.title = contact.getTitle();
		this.firstName = contact.getFirstName();
		this.lastName = contact.getLastName();
		this.dateOfBirth = contact.getDateOfBirth();
		this.phoneNumbers = Lists.newArrayList(contact.getPhoneNumbers());
		this.addresses = Lists.newArrayList(contact.getAddresses());
	}
	
	@JsonCreator
	public Contact(
			@JsonProperty("title") Title title,
			@JsonProperty("firstName") String firstName,
			@JsonProperty("lastName") String lastName,
			@JsonProperty("dateOfBirth") String dateOfBirth,
			@JsonProperty("phoneNumber") List<String> phoneNumbers,
			@JsonProperty("address") List<Address> addresses) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = formatStringToLocalDate(dateOfBirth);
		this.phoneNumbers = phoneNumbers;
		this.addresses = addresses;
	}
	
	public String getId() {
		return id;
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
	
	public boolean hasPhoneNumber() {
		if(phoneNumbers == null) {
			return false;
		}
		return phoneNumbers.size() > 0;
	}
	
	public boolean hasAddress() {
		if(addresses == null) {
			return false;
		}
		return addresses.size() > 0;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	public static Contact createEmptyContact() {
		Contact contact = new Contact("", "", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), Lists.newArrayList(), Lists.newArrayList());
		return contact;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Contact other = (Contact) obj;
		return Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName)
				&& Objects.equals(id, other.id);
	}
	
	private LocalDate formatStringToLocalDate(String dateOfBirth) {
		return Strings.isNullOrEmpty(dateOfBirth) ? LocalDate.now() : LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	@JsonIgnore
	public boolean checkIfDateIsWithinRange(String startDate, String endDate) {
		LocalDate formattedStartDate = formatStringToLocalDate(startDate);
		LocalDate formattedEndDate = formatStringToLocalDate(endDate);
		return !(dateOfBirth.isBefore(formattedStartDate) || dateOfBirth.isAfter(formattedEndDate));
	}

	@Override
	public String toString() {
		return "Contact [title=" + title + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", dateOfBirth=" + dateOfBirth + ", phoneNumbers=" + phoneNumbers + ", addresses=" + addresses + "]";
	}
	
}
