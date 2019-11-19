package com.b2international.phonebook3.rcp.contact;

import java.util.Collection;

import com.b2international.phonebook3.rcp.redux.Action;

public class BulkRemoveAction implements Action {
	
	private final Collection<String> contactIdsToRemove;
	
	public BulkRemoveAction(Collection<String> contactIdsToRemove) {
		this.contactIdsToRemove = contactIdsToRemove;
	}

	public Collection<String> getContactIdsToRemove() {
		return contactIdsToRemove;
	}
	
}
