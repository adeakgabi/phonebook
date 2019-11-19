package com.b2international.phonebook3.rcp.redux;

import java.util.Map;
import java.util.Set;

import org.eclipse.ui.forms.editor.FormEditor;

import com.b2international.phonebook3.rcp.model.Contact;

public class State {
	
	private final Map<String, Contact> contactsMap;
	private final Set<FormEditor> editorState;
	
	public State(Map<String, Contact> contactsMap, Set<FormEditor> editorState) {
		this.contactsMap = contactsMap;
		this.editorState = editorState;
	}
	
	public Map<String, Contact> getContactsMap() {
		return contactsMap;
	}
	
	public Contact getContact(String contactId) {
		return getContactsMap().get(contactId);
	}
	
	public Set<FormEditor> getEditorState() {
		return editorState;
	}
	
	@Override
	public String toString() {
		StringBuilder contacts = new StringBuilder();
		for(Contact contact : contactsMap.values()) {
			contacts.append(contact.getFirstName() + " " + contact.getLastName() + ", ");
		}
		contacts.append(editorState.toString());
		return contacts.toString();
	}

}
