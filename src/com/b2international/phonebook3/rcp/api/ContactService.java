package com.b2international.phonebook3.rcp.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.stream.Collectors;

import com.b2international.phonebook3.rcp.exceptions.ContactNotFoundException;
import com.b2international.phonebook3.rcp.exceptions.WrongInputException;
import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.model.Title;

public class ContactService extends Observable {
	
	private static volatile ContactService contactService;
	
	private final Map<String, Contact> contactsMap = new HashMap<>();
	
	private ContactService() {
		// Prevent instantiations
		if(contactService != null) {
			throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
		}
	}
	
	public static ContactService getInstance() {
		if(contactService == null) {
			synchronized (ContactService.class) {
                if (contactService == null) {
                	contactService = new ContactService();
                }
            }
		}
		return contactService;
	}
	
	protected ContactService readResolve() {
		return getInstance();
	}
	
	public Contact createContact(Title title, String firstName, String lastName, String dateOfBirth, List<String> phoneNumber, List<Address> addresses)
		throws WrongInputException {
		if(firstName == null || firstName.isEmpty()) {
			throw new WrongInputException("Name can't be null!");
		}
		
		final Contact contact;
		if (title == null) {
			contact = new Contact(firstName, lastName, dateOfBirth, phoneNumber, addresses);
		} else {
			contact = new Contact(title, firstName, lastName, dateOfBirth, phoneNumber, addresses);
		}
		saveContact(contact);

		return contact;
	}
	
	public void deleteContact(String id) {
		if (contactsMap.remove(id) != null) {
			updateContactTree();
		}
	}
	
	public void updateContact(ContactUpdate update) throws ContactNotFoundException {
		final String id = update.getId();
		final Contact contact = getContact(id);
		
		if(contact == null) {
			throw new ContactNotFoundException(id);
		}
		
		if (update.update(contact)) {
			updateContactTree();
		}
	}

	public Contact getContact(String id) {
		return contactsMap.get(id);
	}
	
	public void bulkCreate(Collection<Contact> contactsToAdd) {
		for(Contact contact : contactsToAdd) {
			contactsMap.putIfAbsent(contact.getId(), contact);
		}
	}
	
	public void bulkRemove(Collection<String> contactIdsToRemove) throws ContactNotFoundException {
		for(String id : contactIdsToRemove) {
			deleteContact(id);
		}
	}
	
	public Map<String, Contact> getContacts() {
		return contactsMap;
	}
	
	public List<Contact> getFlatContacts() {
		return contactsMap.values().stream().collect(Collectors.toList());
	}
	
	public List<Contact> filter(ContactFilter filter) {
		return contactsMap.values().stream()
				.filter(filter::byTitle)
				.filter(filter::byFirstName)
				.filter(filter::byLastName)
				.filter(filter::byDayOfBirth)
				.filter(filter::byPhoneNumber)
				.filter(filter::byAddress)
				.collect(Collectors.toList());
	}	
	
	public void saveContact(Contact contact) {
		contactsMap.put(contact.getId(), contact);
		updateContactTree();
	}
	
	private void updateContactTree() {
		setChanged();
		notifyObservers(this);
	}

}
