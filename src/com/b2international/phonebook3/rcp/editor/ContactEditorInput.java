package com.b2international.phonebook3.rcp.editor;

import java.util.Objects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class ContactEditorInput implements IEditorInput {

	private final String contactId;
	
	public ContactEditorInput(String contactId) {
		this.contactId = contactId;
	}

	public String getContactId() {
		return contactId;
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
		return contactId;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return contactId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contactId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ContactEditorInput other = (ContactEditorInput) obj;
		return Objects.equals(contactId, other.contactId);
	}
	
}
