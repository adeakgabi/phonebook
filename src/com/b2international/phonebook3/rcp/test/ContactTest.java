package com.b2international.phonebook3.rcp.test;


import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.model.Title;

class ContactTest {

	@Test
	public void testGetters() {
		final Contact contact = getContact();
		
		assertEquals(contact.getFirstName(), "Karen");
		assertEquals(contact.getLastName(), "Wilkinson");
		assertEquals(contact.getDateOfBirth(), "19981225");
	}

	@Test
	public void testSetters() {
		final Contact contactJohn = getContact();
		
		contactJohn.setFirstName("John");
		contactJohn.setLastName("Doe");
		contactJohn.setDateOfBirth(LocalDate.now());
		
		assertEquals(contactJohn.getFirstName(), "John");
		assertEquals(contactJohn.getLastName(), "Doe");
		assertEquals(contactJohn.getDateOfBirth(), "19891002");
	}
	
	private Contact getContact() {
		final Address address = new Address("Arizona", "85012", "Phoenix", "1989 Preston Street");
		final List<Address> addresses = new ArrayList<>();
		addresses.add(address);
		final List<String> phoneNumbers = new ArrayList<>();
		phoneNumbers.add("1-202-555-0118");
		final Contact contact = new Contact(Title.MRS, "Karen", "Wilkinson", "19981225", phoneNumbers, addresses);
		return contact;
	}

}
