package com.b2international.phonebook3.rcp.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.eclipse.ui.forms.editor.FormEditor;
import org.junit.BeforeClass;
import org.junit.Test;

import com.b2international.phonebook3.rcp.api.Store;
import com.b2international.phonebook3.rcp.contact.CreateContactAction;
import com.b2international.phonebook3.rcp.model.Title;
import com.b2international.phonebook3.rcp.redux.State;
import com.b2international.phonebook3.rcp.redux.StateReducer;
import com.google.common.collect.Lists;

public class StoreTest {
	private static StateReducer reducer;
	private static Set<FormEditor> editorState;
	private static State state;
	private static Store store;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		reducer = new StateReducer();
		editorState = new HashSet<>();
		state = new State(new HashMap<>(), editorState);
		store = new Store<>(state, reducer);
	}

	@Test
	public void test() {
		String id = UUID.randomUUID().toString();
		store.dispatch(new CreateContactAction(Title.DEFAULT, "firstName", "lastName", "1999-01-01", Lists.newArrayList(), Lists.newArrayList()));
		assertEquals(1, state.getContactsMap().size());
		assertEquals( "firstName", state.getContact(id).getFirstName());
		assertEquals("lastName", state.getContact(id).getLastName());
	}

}
