package com.b2international.phonebook3.rcp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.osgi.container.ModuleContainerAdaptor.ContainerEvent;
import org.eclipse.ui.forms.editor.FormEditor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.b2international.phonebook3.rcp.Activator;
import com.b2international.phonebook3.rcp.api.ContactFilter;
import com.b2international.phonebook3.rcp.api.Store;
import com.b2international.phonebook3.rcp.contact.BulkCreateAction;
import com.b2international.phonebook3.rcp.contact.BulkRemoveAction;
import com.b2international.phonebook3.rcp.contact.ContactUpdateAction;
import com.b2international.phonebook3.rcp.contact.CreateContactAction;
import com.b2international.phonebook3.rcp.contact.DeleteContactAction;
import com.b2international.phonebook3.rcp.exceptions.ContactNotFoundException;
import com.b2international.phonebook3.rcp.exceptions.WrongInputException;
import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.model.Title;
import com.b2international.phonebook3.rcp.model.Contact.ContactBuilder;
import com.b2international.phonebook3.rcp.redux.State;
import com.b2international.phonebook3.rcp.redux.StateReducer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StoreReducerTest {
	
	private StateReducer reducer;
	private Set<FormEditor> editorState;
	private Store store;
	
	@Before
	public void init() {
		reducer = new StateReducer();
		store = new Store<>(new State(Collections.emptyMap(), Collections.emptySet()), reducer);
	}
	
	@Test
	public void testCreateContactAction() {
		store.dispatch(new CreateContactAction(Title.DEFAULT, "firstName", "lastName", "1999-01-01", Lists.newArrayList(), Lists.newArrayList()));
		State newState = (State) store.getState();
		assertEquals(1, newState.getContactsMap().size());
		
	}

	@Test
	public void testBulkCreationAction() {
		final Contact contact1 = Contact.createEmptyContact();
		final Contact contact2 = Contact.createEmptyContact();
		final Contact contact3 = Contact.createEmptyContact();
		Collection<Contact> testContacts = new ArrayList<>();
		testContacts.add(contact1);
		testContacts.add(contact2);
		testContacts.add(contact3);
		store.dispatch(new BulkCreateAction(testContacts));
		State newState = (State) store.getState();
		assertTrue(newState.getContactsMap().containsValue(contact1));
		assertTrue(newState.getContactsMap().containsValue(contact2));
		assertTrue(newState.getContactsMap().containsValue(contact3));
		
	}
	
	@Test
	public void testBulkRemoveAction() {
		final Contact c1 = Contact.createEmptyContact();
		final Contact c2 = Contact.createEmptyContact();
		final Contact c3 = Contact.createEmptyContact();
		final Contact c4 = Contact.createEmptyContact();
		Collection<Contact> testContacts = new ArrayList<>();
		testContacts.add(c1);
		testContacts.add(c2);
		testContacts.add(c3);
		testContacts.add(c4);
		store.dispatch(new BulkCreateAction(testContacts));

		Collection<String> contactsToRemove = new ArrayList<>();
		contactsToRemove.add(c1.getId());
		contactsToRemove.add(c2.getId());
		
		store.dispatch(new BulkRemoveAction(contactsToRemove));
		State newState = (State) store.getState();
		assertTrue(newState.getContactsMap().containsValue(c4));
		assertTrue(newState.getContactsMap().containsValue(c3));
		assertTrue(!newState.getContactsMap().containsValue(c2));
		assertTrue(!newState.getContactsMap().containsValue(c1));
	}
	
	@Test
	public void testUpdateAction() {
		CreateContactAction createContactAction = new CreateContactAction(Title.DEFAULT, "firstName", "lastName", "1999-01-01",
				Lists.newArrayList(), Lists.newArrayList());
		store.dispatch(createContactAction);
		String id = createContactAction.getId();
		store.dispatch(ContactUpdateAction.builder(id)
				.firstName("Joey")
				.lastName("Smith")
				.build());
		State newState = (State) store.getState();
		assertEquals("Joey", newState.getContactsMap().get(id).getFirstName());
		assertEquals("Smith", newState.getContactsMap().get(id).getLastName());
	}
	
	@After
	public void clearStore() {
		reducer = new StateReducer();
		store = new Store<>(new State(Collections.emptyMap(), Collections.emptySet()), reducer);
	}
	
//	@Test
//	public void testUpdateWithEmptyTitleAndFirstName() throws WrongInputException, ContactNotFoundException {
//		final Contact contact1 = createContact(Title.MR, "John", "Doe", "19990101");
//
//		ContactUpdateAction contactUpdate = new ContactUpdateAction.ContactUpdateBuilder(contact1.getId())
//				.title(null)
//				.firstName("")
//				.build();
//		contactService.updateContact(contactUpdate);
//		
//		assertEquals("Mr", contact1.getTitle());
//		assertEquals("John", contact1.getFirstName());
//		assertTrue(contact1.getPhoneNumbers().contains("000"));
//	}
//	
//	@Test(expected = WrongInputException.class)
//	public void testUpdateWithEmptyPhoneField() throws WrongInputException, ContactNotFoundException {
//		final Contact contact1 = createContact(Title.MR, "John", "Doe", "19990101");
//
//		ContactUpdateAction contactUpdate = new ContactUpdateAction.ContactUpdateBuilder(contact1.getId())
//				.phoneNumbers(new ArrayList<>())
//				.build();
//		contactService.updateContact(contactUpdate);
//		assertTrue(contact1.getPhoneNumbers().contains("000"));
//	}
//	
//	@Test(expected = WrongInputException.class)
//	public void testUpdateWithEmptyAddress() throws WrongInputException, ContactNotFoundException {
//		final Contact contact1 = createContact(Title.MR, "John", "Doe", "19990101");
//
//		ContactUpdateAction contactUpdate = new ContactUpdateAction.ContactUpdateBuilder(contact1.getId())
//				.addresses(new ArrayList<>())
//				.build();
//		contactService.updateContact(contactUpdate);
//	}
//	
//	@Test
//	public void testFilterForTitle() throws WrongInputException, ParseException {
//		createContact(Title.MR, "John", "Doe", "19990101");
//		createContact(Title.MR, "Jack", "Smith", "20001010");
//		createContact(Title.MRS, "Joe", "Smith", "19901101");
//		createContact(null, "Jannis", "Lawrence", "20101010");
//		
//		ContactFilter contactFilterForMrs = new ContactFilter.ContactFilterBuilder()
//				.title("Mrs")
//				.build();
//		final List<Contact> filteredContactsForMrs = contactService.filter(contactFilterForMrs);
//		assertEquals(1, filteredContactsForMrs.size());
//		
//		ContactFilter contactFilterForMr = new ContactFilter.ContactFilterBuilder()
//				.title("Mr")
//				.build();
//		final List<Contact> filteredContactsForMr = contactService.filter(contactFilterForMr);
//		assertEquals(2, filteredContactsForMr.size());
//	}
//	
//	@Test
//	public void testFilterForFirstName() throws WrongInputException, ParseException {
//		final Contact contactJoe = createContact(Title.MR, "Joe", "Doe", "19990101");
//		createContact(Title.MR, "Jan", "Smith", "20001010");
//		createContact(Title.MRS, "Jenny", "Smith", "19901101");
//		createContact(null, "Jannis", "Lawrence", "20101010");
//		
//		ContactFilter contactFilterForJoe = new ContactFilter.ContactFilterBuilder()
//				.firstName("Joe")
//				.build();
//		final List<Contact> filteredContactsForJoe = contactService.filter(contactFilterForJoe);
//		assertEquals(1, filteredContactsForJoe.size());
//		assertEquals(contactJoe, filteredContactsForJoe.get(0));
//	}
//	
//	@Test
//	public void testFilterForNonExistingFirstName() throws WrongInputException, ParseException {
//		createContact(Title.MR, "Joe", "Doe", "19990101");
//		createContact(Title.MR, "Jan", "Smith", "20001010");
//		createContact(Title.MRS, "Joe", "Smith", "19901101");
//		createContact(null, "Jannis", "Lawrence", "20101010");
//		
//		ContactFilter contactFilterForJack = new ContactFilter.ContactFilterBuilder()
//				.firstName("Jack")
//				.build();
//		final List<Contact> filteredContactsForJack = contactService.filter(contactFilterForJack);
//		assertEquals(0, filteredContactsForJack.size());
//	}	
//	
//	@Test
//	public void testFilterForLastName() throws WrongInputException, ParseException {
//		final Contact contactSmith1 = createContact(Title.MR, "Jan", "Smith", "20001010");
//		final Contact contactSmith2 = createContact(Title.MRS, "Joe", "Smith", "19901101");
//		createContact(Title.MRS, "Joe", "Doe", "19990101");
//		createContact(null, "Jannis", "Lawrence", "20101010");
//		
//		ContactFilter contactFilterForSmith = new ContactFilter.ContactFilterBuilder()
//				.lastName("smith")
//				.build();
//		final List<Contact> filteredContactsForSmith = contactService.filter(contactFilterForSmith);
//		assertEquals(2, filteredContactsForSmith.size());
//		assertTrue(filteredContactsForSmith.contains(contactSmith1));
//		assertTrue(filteredContactsForSmith.contains(contactSmith2));
//	}
//	
//	@Test
//	public void testFilterForDateOfBirth() throws WrongInputException, ParseException {
//		createContact(Title.MRS, "Joe", "Doe", "19990101");
//		createContact(Title.MRS, "Jan", "Smith", "20001010");
//		createContact(Title.MRS, "Joe", "Smith", "19901101");
//		createContact(null, "Jannis", "Lawrence", "20001010");
//		
//		ContactFilter contactFilterForDateOfBirth = new ContactFilter.ContactFilterBuilder()
//				.startDate("19981201")
//				.endDate("20010101")
//				.build();
//		final List<Contact> filteredContactsForDateOfBirth = contactService.filter(contactFilterForDateOfBirth);
//		assertEquals(3, filteredContactsForDateOfBirth.size());
//		
//		ContactFilter contactFilterForDateOfBirth1 = new ContactFilter.ContactFilterBuilder()
//				.startDate("20001010")
//				.endDate("20001010")
//				.build();
//		final List<Contact> filteredContactsForDateOfBirth1 = contactService.filter(contactFilterForDateOfBirth1);
//		assertEquals(2, filteredContactsForDateOfBirth1.size());
//	}
//	
//	@Test
//	public void testFilterForPhoneNumber() throws WrongInputException, ParseException {
//		final Contact contact1 = createContact(Title.MRS, "Joe", "Doe", "19990101");
//		final Contact contact2 = createContact(Title.MRS, "Jan", "Smith", "20001010");
//		final Contact contactWithDifferentPhoneNumber = createContact(Title.MRS, "Joe", "Smith", "19901101");
//		final List<String> phoneNumber = new ArrayList<>();
//		phoneNumber.add("0123456");
//		contact1.setPhoneNumbers(phoneNumber);
//		contact2.setPhoneNumbers(phoneNumber);
//		
//		ContactFilter contactFilterForPhoneNumber = new ContactFilter.ContactFilterBuilder()
//				.phoneNumber("0123456")
//				.build();
//		final List<Contact> filteredContactsForPhoneNumber = contactService.filter(contactFilterForPhoneNumber);
//		
//		assertEquals(2, filteredContactsForPhoneNumber.size());
//		assertTrue(filteredContactsForPhoneNumber.contains(contact1));
//		assertTrue(filteredContactsForPhoneNumber.contains(contact2));
//		assertTrue(!filteredContactsForPhoneNumber.contains(contactWithDifferentPhoneNumber));
//	}
//	
//	@Test
//	public void testFilterForAddress() throws WrongInputException, ParseException {
//		final Contact contact1 = createContact(Title.MS, "Joe", "Doe", "19990101");
//		final Contact contact2 = createContact(Title.MS, "Jan", "Smith", "20001010");
//		final Contact contactWithDifferentAddress = createContact(Title.MS, "Joe", "Smith", "19901101");
//		List<Address> addresses = new ArrayList<>();
//		addresses.add(new Address("5700","","",""));
//		
//		contact1.setAddresses(addresses);
//		contact2.setAddresses(addresses);
//		
//		ContactFilter contactFilterForAddress = new ContactFilter.ContactFilterBuilder()
//				.addresses("5700")
//				.build();
//		final List<Contact> filteredContactsForAddress = contactService.filter(contactFilterForAddress);
//		assertEquals(2, filteredContactsForAddress.size());
//		assertTrue(filteredContactsForAddress.contains(contact1));
//		assertTrue(filteredContactsForAddress.contains(contact2));
//		assertTrue(!filteredContactsForAddress.contains(contactWithDifferentAddress));
//	}

	
}
