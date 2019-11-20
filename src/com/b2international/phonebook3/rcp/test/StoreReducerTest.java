package com.b2international.phonebook3.rcp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.b2international.phonebook3.rcp.api.Store;
import com.b2international.phonebook3.rcp.contact.BulkCreateAction;
import com.b2international.phonebook3.rcp.contact.BulkRemoveAction;
import com.b2international.phonebook3.rcp.contact.ContactUpdateAction;
import com.b2international.phonebook3.rcp.contact.CreateContactAction;
import com.b2international.phonebook3.rcp.contact.DeleteContactAction;
import com.b2international.phonebook3.rcp.exceptions.WrongInputException;
import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.model.Title;
import com.b2international.phonebook3.rcp.redux.State;
import com.b2international.phonebook3.rcp.redux.StateReducer;
import com.google.common.collect.Lists;

public class StoreReducerTest {
	
	private StateReducer reducer;
	private Store<State> store;
	
	@Before
	public void init() {
		reducer = new StateReducer();
		store = new Store<>(new State(Collections.emptyMap(), Collections.emptySet()), reducer);
	}
	
	@After
	public void clearStore() {
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
	public void testDeleteContactAction() {
		CreateContactAction createContactAction1 = new CreateContactAction(Title.DEFAULT, "firstName", "lastName", "1999-01-01",
				Lists.newArrayList(), Lists.newArrayList());
		store.dispatch(createContactAction1);
		String idDeleted = createContactAction1.getId();
		CreateContactAction createContactAction2 = new CreateContactAction(Title.DEFAULT, "firstName", "lastName", "1999-01-01",
				Lists.newArrayList(), Lists.newArrayList());
		store.dispatch(createContactAction2);
		String id = createContactAction2.getId();
		store.dispatch(new DeleteContactAction(idDeleted));
		State newState = (State) store.getState();
		assertTrue(!newState.getContactsMap().containsKey(idDeleted));
		assertTrue(newState.getContactsMap().containsKey(id));
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

	@Test
	public void testUpdateWithEmptyFirstName() {
		CreateContactAction createContactAction = new CreateContactAction(Title.DEFAULT, "John", "Doe", "1999-01-01",
				Lists.newArrayList(), Lists.newArrayList());
		store.dispatch(createContactAction);
		String id = createContactAction.getId();
		store.dispatch(ContactUpdateAction.builder(id)
				.firstName("")
				.build());
		State newState = (State) store.getState();
		assertEquals("John", newState.getContactsMap().get(id).getFirstName());
	}
	
	@Test
	public void testUpdateWithEmptyLastName() {
		CreateContactAction createContactAction = new CreateContactAction(Title.DEFAULT, "John", "Doe", "1999-01-01",
				Lists.newArrayList(), Lists.newArrayList());
		store.dispatch(createContactAction);
		String id = createContactAction.getId();
		store.dispatch(ContactUpdateAction.builder(id)
				.lastName("")
				.build());
		State newState = (State) store.getState();
		assertEquals("Doe", newState.getContactsMap().get(id).getLastName());
	}
	
	@Test
	public void testUpdateWithPhoneNumber() {
		List<String> phoneNumbers = Lists.newArrayList();
		phoneNumbers.add("0-124-123-1234");
		List<String> phoneNumbersToAdd = Lists.newArrayList();
		phoneNumbersToAdd.add("1111");
		CreateContactAction createContactAction = new CreateContactAction(Title.DEFAULT, "John", "Doe", "1999-01-01",
				phoneNumbers, Lists.newArrayList());
		store.dispatch(createContactAction);
		String id = createContactAction.getId();
		try {
			store.dispatch(ContactUpdateAction.builder(id)
					.phoneNumbers(phoneNumbersToAdd)
					.build());
		} catch (WrongInputException e) {
			e.printStackTrace();
		}
		State newState = (State) store.getState();
		assertTrue(newState.getContactsMap().get(id).getPhoneNumbers().contains("1111"));
	}

}
