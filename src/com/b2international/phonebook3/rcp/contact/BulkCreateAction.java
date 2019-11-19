package com.b2international.phonebook3.rcp.contact;

import java.util.Collection;

import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.redux.Action;

public class BulkCreateAction implements Action {
	
	private final Collection<Contact> contactsToAdd;
	
	public BulkCreateAction(Collection<Contact> contactsToAdd) {
		this.contactsToAdd = contactsToAdd;
	}

	public Collection<Contact> getContactsToAdd() {
		return contactsToAdd;
	}
	
}
