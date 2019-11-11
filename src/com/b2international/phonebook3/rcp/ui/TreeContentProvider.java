package com.b2international.phonebook3.rcp.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.b2international.phonebook3.rcp.model.Contact;

public class TreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		return ArrayContentProvider.getInstance().getElements(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Contact) {
			Contact contact = (Contact) parentElement;
			return contact.getAddresses().toArray();
		}
		return null;
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
