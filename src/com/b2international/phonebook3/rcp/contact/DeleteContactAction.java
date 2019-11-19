package com.b2international.phonebook3.rcp.contact;

import com.b2international.phonebook3.rcp.redux.Action;

public class DeleteContactAction implements Action {

	private final String id;

	public DeleteContactAction(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
}
