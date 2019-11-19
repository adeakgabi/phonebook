package com.b2international.phonebook3.rcp.ui;

import java.util.Collection;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.b2international.phonebook3.rcp.model.Contact;

public class TreeContentProvider implements ITreeContentProvider {

	@SuppressWarnings("rawtypes")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Collection<?>) {
			return ((Collection) inputElement).toArray();
		} else if (inputElement instanceof Map<?, ?>) {
			return ((Map) inputElement).values().toArray();
		}
		return new Object[]{};
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Contact) {
			Contact contact = (Contact) parentElement;
			return contact.getAddresses().toArray();
		}
		return new Object[]{};
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
	     Object[] children = getChildren(element);
	     return children != null && children.length > 0;
	}
	
}
