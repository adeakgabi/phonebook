package com.b2international.phonebook3.rcp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.b2international.phonebook3.rcp.api.ContactFilter;
import com.b2international.phonebook3.rcp.api.ContactService;
import com.b2international.phonebook3.rcp.api.ContactUpdate;
import com.b2international.phonebook3.rcp.exceptions.ContactNotFoundException;
import com.b2international.phonebook3.rcp.exceptions.WrongInputException;
import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.model.Title;

public class ContactServiceTest {
	
	private ContactService contactService = ContactService.getInstance();

	@Test(expected = WrongInputException.class)
	public void testContactCreationWithEmptyName() throws WrongInputException {
		List<Address> addresses = new ArrayList<>();
		addresses.add(new Address("","","",""));
		List<String> number = new ArrayList<>();
		number.add("000");
		contactService.createContact(null, "", "lastName", "dateOfBirth", number, addresses);
		assertEquals(1, contactService.getContacts().size());
	}
	
	@Test
	public void testContactCreation() throws WrongInputException {
		List<Address> addresses = new ArrayList<>();
		addresses.add(new Address("","","",""));
		List<String> number = new ArrayList<>();
		number.add("000");
		Contact testContact = contactService.createContact(null, "firstName", "lastName", "dateOfBirth", number, addresses);
		assertEquals(1, contactService.getContacts().size());
		assertEquals( "firstName", testContact.getFirstName());
		assertEquals("lastName", testContact.getLastName());
		assertEquals("dateOfBirth", testContact.getDateOfBirth());
	}
	
	@Test(expected = ContactNotFoundException.class)
	public void deleteContact_withNullid_throwsException() throws ContactNotFoundException {
		contactService.deleteContact(null);
	}
	
	@Test
	public void testBulkCreation()  throws WrongInputException{
		final Contact contact1 = createContact(Title.MRS, "John", "lastName", "dateOfBirth");
		final Contact contact2 = createContact(null, "Jack", "lastName", "dateOfBirth");
		final Contact contact3 = createContact(null, "Joe", "lastName", "dateOfBirth");
		Collection<Contact> testContacts = new ArrayList<>();
		testContacts.add(contact1);
		testContacts.add(contact2);
		testContacts.add(contact3);
		
		contactService.bulkCreate(testContacts);
		assertEquals(3, contactService.getContacts().size());
		assertTrue(contactService.getContacts().containsValue(contact1));
		assertTrue(contactService.getContacts().containsValue(contact2));
		assertTrue(contactService.getContacts().containsValue(contact3));
		
	}
	
	@Test
	public void testBulkRemove() throws ContactNotFoundException, WrongInputException {
		final Contact contact1 = createContact(Title.MR, "John", "lastName", "dateOfBirth");
		final Contact contact2 = createContact(Title.MR, "Jack", "lastName", "dateOfBirth");
		final Contact contact3 = createContact(Title.MR, "Joe", "lastName", "dateOfBirth");
		final Contact contact4 = createContact(Title.MR, "Jannis", "lastName", "dateOfBirth");
		
		Collection<String> contactsToRemove = new ArrayList<>();
		contactsToRemove.add(contact1.getId());
		contactsToRemove.add(contact2.getId());
		
		contactService.bulkRemove(contactsToRemove);
		assertEquals(2, contactService.getContacts().size());
		assertTrue(contactService.getContacts().containsValue(contact3));
		assertTrue(contactService.getContacts().containsValue(contact4));
		assertTrue(!contactService.getContacts().containsValue(contact1));
		assertTrue(!contactService.getContacts().containsValue(contact2));
	}
	
	@Test
	public void testUpdate() throws WrongInputException, ContactNotFoundException {
		final Contact contact1 = createContact(Title.MRS, "Josh", "Doe", "19990101");

		ContactUpdate contactUpdate = new ContactUpdate.ContactUpdateBuilder(contact1.getId())
				.title(Title.MS)
				.firstName("Joe")
				.build();
		contactService.updateContact(contactUpdate);
		
		assertEquals("Ms", contact1.getTitle());
		assertEquals("Joe", contact1.getFirstName());
	}
	
	@Test
	public void testUpdateWithEmptyTitleAndFirstName() throws WrongInputException, ContactNotFoundException {
		final Contact contact1 = createContact(Title.MR, "John", "Doe", "19990101");

		ContactUpdate contactUpdate = new ContactUpdate.ContactUpdateBuilder(contact1.getId())
				.title(null)
				.firstName("")
				.build();
		contactService.updateContact(contactUpdate);
		
		assertEquals("Mr", contact1.getTitle());
		assertEquals("John", contact1.getFirstName());
		assertTrue(contact1.getPhoneNumbers().contains("000"));
	}
	
	@Test(expected = WrongInputException.class)
	public void testUpdateWithEmptyPhoneField() throws WrongInputException, ContactNotFoundException {
		final Contact contact1 = createContact(Title.MR, "John", "Doe", "19990101");

		ContactUpdate contactUpdate = new ContactUpdate.ContactUpdateBuilder(contact1.getId())
				.phoneNumbers(new ArrayList<>())
				.build();
		contactService.updateContact(contactUpdate);
		assertTrue(contact1.getPhoneNumbers().contains("000"));
	}
	
	@Test(expected = WrongInputException.class)
	public void testUpdateWithEmptyAddress() throws WrongInputException, ContactNotFoundException {
		final Contact contact1 = createContact(Title.MR, "John", "Doe", "19990101");

		ContactUpdate contactUpdate = new ContactUpdate.ContactUpdateBuilder(contact1.getId())
				.addresses(new ArrayList<>())
				.build();
		contactService.updateContact(contactUpdate);
	}
	
	@Test
	public void testFilterForTitle() throws WrongInputException, ParseException {
		createContact(Title.MR, "John", "Doe", "19990101");
		createContact(Title.MR, "Jack", "Smith", "20001010");
		createContact(Title.MRS, "Joe", "Smith", "19901101");
		createContact(null, "Jannis", "Lawrence", "20101010");
		
		ContactFilter contactFilterForMrs = new ContactFilter.ContactFilterBuilder()
				.title("Mrs")
				.build();
		final List<Contact> filteredContactsForMrs = contactService.filter(contactFilterForMrs);
		assertEquals(1, filteredContactsForMrs.size());
		
		ContactFilter contactFilterForMr = new ContactFilter.ContactFilterBuilder()
				.title("Mr")
				.build();
		final List<Contact> filteredContactsForMr = contactService.filter(contactFilterForMr);
		assertEquals(2, filteredContactsForMr.size());
	}
	
	@Test
	public void testFilterForFirstName() throws WrongInputException, ParseException {
		final Contact contactJoe = createContact(Title.MR, "Joe", "Doe", "19990101");
		createContact(Title.MR, "Jan", "Smith", "20001010");
		createContact(Title.MRS, "Jenny", "Smith", "19901101");
		createContact(null, "Jannis", "Lawrence", "20101010");
		
		ContactFilter contactFilterForJoe = new ContactFilter.ContactFilterBuilder()
				.firstName("Joe")
				.build();
		final List<Contact> filteredContactsForJoe = contactService.filter(contactFilterForJoe);
		assertEquals(1, filteredContactsForJoe.size());
		assertEquals(contactJoe, filteredContactsForJoe.get(0));
	}
	
	@Test
	public void testFilterForNonExistingFirstName() throws WrongInputException, ParseException {
		createContact(Title.MR, "Joe", "Doe", "19990101");
		createContact(Title.MR, "Jan", "Smith", "20001010");
		createContact(Title.MRS, "Joe", "Smith", "19901101");
		createContact(null, "Jannis", "Lawrence", "20101010");
		
		ContactFilter contactFilterForJack = new ContactFilter.ContactFilterBuilder()
				.firstName("Jack")
				.build();
		final List<Contact> filteredContactsForJack = contactService.filter(contactFilterForJack);
		assertEquals(0, filteredContactsForJack.size());
	}	
	
	@Test
	public void testFilterForLastName() throws WrongInputException, ParseException {
		final Contact contactSmith1 = createContact(Title.MR, "Jan", "Smith", "20001010");
		final Contact contactSmith2 = createContact(Title.MRS, "Joe", "Smith", "19901101");
		createContact(Title.MRS, "Joe", "Doe", "19990101");
		createContact(null, "Jannis", "Lawrence", "20101010");
		
		ContactFilter contactFilterForSmith = new ContactFilter.ContactFilterBuilder()
				.lastName("smith")
				.build();
		final List<Contact> filteredContactsForSmith = contactService.filter(contactFilterForSmith);
		assertEquals(2, filteredContactsForSmith.size());
		assertTrue(filteredContactsForSmith.contains(contactSmith1));
		assertTrue(filteredContactsForSmith.contains(contactSmith2));
	}
	
	@Test
	public void testFilterForDateOfBirth() throws WrongInputException, ParseException {
		createContact(Title.MRS, "Joe", "Doe", "19990101");
		createContact(Title.MRS, "Jan", "Smith", "20001010");
		createContact(Title.MRS, "Joe", "Smith", "19901101");
		createContact(null, "Jannis", "Lawrence", "20001010");
		
		ContactFilter contactFilterForDateOfBirth = new ContactFilter.ContactFilterBuilder()
				.startDate("19981201")
				.endDate("20010101")
				.build();
		final List<Contact> filteredContactsForDateOfBirth = contactService.filter(contactFilterForDateOfBirth);
		assertEquals(3, filteredContactsForDateOfBirth.size());
		
		ContactFilter contactFilterForDateOfBirth1 = new ContactFilter.ContactFilterBuilder()
				.startDate("20001010")
				.endDate("20001010")
				.build();
		final List<Contact> filteredContactsForDateOfBirth1 = contactService.filter(contactFilterForDateOfBirth1);
		assertEquals(2, filteredContactsForDateOfBirth1.size());
	}
	
	@Test
	public void testFilterForPhoneNumber() throws WrongInputException, ParseException {
		final Contact contact1 = createContact(Title.MRS, "Joe", "Doe", "19990101");
		final Contact contact2 = createContact(Title.MRS, "Jan", "Smith", "20001010");
		final Contact contactWithDifferentPhoneNumber = createContact(Title.MRS, "Joe", "Smith", "19901101");
		final List<String> phoneNumber = new ArrayList<>();
		phoneNumber.add("0123456");
		contact1.setPhoneNumbers(phoneNumber);
		contact2.setPhoneNumbers(phoneNumber);
		
		ContactFilter contactFilterForPhoneNumber = new ContactFilter.ContactFilterBuilder()
				.phoneNumber("0123456")
				.build();
		final List<Contact> filteredContactsForPhoneNumber = contactService.filter(contactFilterForPhoneNumber);
		
		assertEquals(2, filteredContactsForPhoneNumber.size());
		assertTrue(filteredContactsForPhoneNumber.contains(contact1));
		assertTrue(filteredContactsForPhoneNumber.contains(contact2));
		assertTrue(!filteredContactsForPhoneNumber.contains(contactWithDifferentPhoneNumber));
	}
	
	@Test
	public void testFilterForAddress() throws WrongInputException, ParseException {
		final Contact contact1 = createContact(Title.MS, "Joe", "Doe", "19990101");
		final Contact contact2 = createContact(Title.MS, "Jan", "Smith", "20001010");
		final Contact contactWithDifferentAddress = createContact(Title.MS, "Joe", "Smith", "19901101");
		List<Address> addresses = new ArrayList<>();
		addresses.add(new Address("5700","","",""));
		
		contact1.setAddresses(addresses);
		contact2.setAddresses(addresses);
		
		ContactFilter contactFilterForAddress = new ContactFilter.ContactFilterBuilder()
				.addresses("5700")
				.build();
		final List<Contact> filteredContactsForAddress = contactService.filter(contactFilterForAddress);
		assertEquals(2, filteredContactsForAddress.size());
		assertTrue(filteredContactsForAddress.contains(contact1));
		assertTrue(filteredContactsForAddress.contains(contact2));
		assertTrue(!filteredContactsForAddress.contains(contactWithDifferentAddress));
	}
	
	private Contact createContact(Title title, String firstName, String lastName, String dateOfBirth) throws WrongInputException {
		List<Address> addresses = new ArrayList<>();
		addresses.add(new Address("1063","","",""));
		List<String> number = new ArrayList<>();
		number.add("000");
		
		return contactService.createContact(title, firstName, lastName, dateOfBirth, number, addresses);
	}
	
}
