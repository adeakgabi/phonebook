package com.b2international.phonebook3.rcp.redux;

import java.util.Map;

import com.b2international.phonebook3.rcp.api.ContactService;
import com.b2international.phonebook3.rcp.model.Contact;

public class ContactsReducer implements Reducer<Map<String, Contact>> {


	@SuppressWarnings("incomplete-switch")
	@Override
	public Map<String, Contact> reduce(Map<String, Contact> state, Action action) {
		if(action instanceof Actions) {
			switch((Actions) action) {
			case LOADFILE:
				return state;
			case UPDATE:
				return ContactService.getInstance().getContacts();
			}
		}
		return state;
	}

}
