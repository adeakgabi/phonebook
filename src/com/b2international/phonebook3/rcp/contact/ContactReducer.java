package com.b2international.phonebook3.rcp.contact;

import java.util.HashMap;
import java.util.Map;

import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.redux.Action;
import com.b2international.phonebook3.rcp.redux.Reducer;

public class ContactReducer implements Reducer<Map<String, Contact>> {

	@Override
	public Map<String, Contact> reduce(Map<String, Contact> state, Action action) {
		if (action instanceof CreateContactAction) {
			final CreateContactAction create = (CreateContactAction) action;
			final Contact newContact = Contact.builder().id(create.getId()).title(create.getTitle())
					.firstName(create.getFirstName()).lastName(create.getLastName())
					.dateOfBirth(Contact.toDate(create.getDateOfBirth())).phoneNumbers(create.getPhoneNumber())
					.addresses(create.getAddresses()).build();

			final Map<String, Contact> newState = new HashMap<>(state);
			newState.put(newContact.getId(), newContact);
			return newState;
		} else if (action instanceof ContactUpdateAction) {
			ContactUpdateAction update = (ContactUpdateAction) action;
			Contact contact = state.get(update.getId());
			Contact updated = update.update(contact);
			if (!updated.equals(contact)) {
				final Map<String, Contact> newState = new HashMap<>(state);
				newState.put(contact.getId(), updated);
				return newState;
			}
		} else if (action instanceof DeleteContactAction) {
			final Map<String, Contact> newState = new HashMap<>(state);
			newState.remove(((DeleteContactAction) action).getId());
			return newState;
		} else if (action instanceof BulkRemoveAction) {
			final Map<String, Contact> newState = new HashMap<>(state);
			for (String id : ((BulkRemoveAction) action).getContactIdsToRemove()) {
				newState.remove(id);
			}
			return newState;
		} else if (action instanceof BulkCreateAction) {
			final Map<String, Contact> newState = new HashMap<>(state);
			for (Contact contact : ((BulkCreateAction) action).getContactsToAdd()) {
				newState.putIfAbsent(contact.getId(), contact);
			}
			return newState;
		}
		return state;
	}

}
