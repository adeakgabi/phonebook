package com.b2international.phonebook3.rcp.redux;

import java.util.Map;
import java.util.Set;

import org.eclipse.ui.forms.editor.FormEditor;

import com.b2international.phonebook3.rcp.contact.ContactReducer;
import com.b2international.phonebook3.rcp.model.Contact;

public class StateReducer implements Reducer<State> {

	@Override
	public State reduce(State state, Action action) {
		Map<String, Contact> contactsMap = new ContactReducer().reduce(state.getContactsMap(), action);
		Set<FormEditor> editorState = new EditorReducer().reduce(state.getEditorState(), action);
		return new State(contactsMap, editorState);
	}

}
