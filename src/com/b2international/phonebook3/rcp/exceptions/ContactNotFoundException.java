package com.b2international.phonebook3.rcp.exceptions;

public class ContactNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ContactNotFoundException(String contactId) {
		super(String.format("Contact with %s id not found", contactId));
	}

}
