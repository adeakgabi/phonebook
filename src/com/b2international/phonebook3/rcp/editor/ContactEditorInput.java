package com.b2international.phonebook3.rcp.editor;

import java.util.Objects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.b2international.phonebook3.rcp.model.Contact;

public class ContactEditorInput implements IEditorInput {

	private Contact contact;
	
	public ContactEditorInput() {
		this.contact = Contact.createEmptyContact();
	}
	
	public ContactEditorInput(Contact contact) {
		this.contact = new Contact(contact);
	}
	
	public Contact getContact() {
		return contact;
	}
	
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return contact.getFirstName() + " " + contact.getLastName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return contact.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(contact.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ContactEditorInput other = (ContactEditorInput) obj;
		return Objects.equals(contact.getId(), other.contact.getId());
	}
	
}
